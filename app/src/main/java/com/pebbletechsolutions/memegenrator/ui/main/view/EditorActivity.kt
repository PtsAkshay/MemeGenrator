package com.pebbletechsolutions.memegenrator.ui.main.view


import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pebbletechsolutions.memegenrator.databinding.ActivityEditorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pebbletechsolutions.memegenrator.databinding.GetNameDialogLytBinding
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.R.attr.data
import android.app.Activity
import android.content.DialogInterface

import android.provider.MediaStore

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.format.DateFormat
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.google.android.material.slider.Slider
import top.defaults.colorpicker.ColorPickerView
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import java.util.*
import android.R
import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.widget.*

import java.io.*


class EditorActivity : AppCompatActivity() {

    private var mXDelta = 0
    private var mYDelta = 0
    private var mRootWidth = 0
    private var mRootHeight = 0

    private var xDelta: Int = 0
    private var yDelta: Int = 0
    private lateinit var touchListener: View.OnTouchListener
    private var gatedUri = ""
    private lateinit var mOnTouchListener: View.OnTouchListener

    private lateinit var editBind: ActivityEditorBinding
    var bsImgUri = ""
    var passMoreBundle = Bundle()
    private val IMAGE_REQ = 44
    var textToAddImg = ""
    var fontSize: Float = 0f
    var addedTxt = 0
    private lateinit var addingTextView: TextView
    private lateinit var addingTextViewTwo: TextView
    private lateinit var addNameDialog: MaterialAlertDialogBuilder
    private var nameDialogBind: GetNameDialogLytBinding? = null
    private lateinit var colorPickerView: ColorPickerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBind = ActivityEditorBinding.inflate(layoutInflater)
        val view = editBind.root
        setContentView(view)
        addingTextView = TextView(this)
        addingTextViewTwo = TextView(this)
        colorPickerView = ColorPickerView(this)

        var intent = intent
        val HomeBS = intent.extras?.get("homeBS")
        val TakenFromHome = intent.extras?.get("takenFromHome")
        if (HomeBS == true){
            bsImgUri = intent.extras?.get("fromHomeBs") as String
            Log.e("im", bsImgUri)
//        passMoreBundle.putString("SlctImgUri", bsImgUri)

            Glide.with(this).load(bsImgUri).into(editBind.editImg)
        }else
            if (intent.extras?.get("FromHomeFabBtn") == true){
                if (TakenFromHome == true){
                    val takenUri = intent.extras?.get("TakenPicture")
                    Log.e("takenUri", takenUri.toString())
                    Glide.with(this).load(takenUri).into(editBind.editImg)
//                    editBind.editImg.setImageURI(takenUri as Uri)
                }else {
                    val pickedUri = intent.extras?.get("pickedImage")
                    editBind.editImg.setImageURI(pickedUri as Uri)

                }
            }


//        replaceFragment(EditImageFrag())


        addNameDialog = MaterialAlertDialogBuilder(this)


        mOnTouchListener =
            View.OnTouchListener { view, event ->
                val xScreenTouch = event.rawX.toInt() // x location relative to the screen
                val yScreenTouch = event.rawY.toInt() // y location relative to the screen
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> {
                        val lParams = view.layoutParams as FrameLayout.LayoutParams
                        mXDelta = xScreenTouch - lParams.leftMargin
                        mYDelta = yScreenTouch - lParams.topMargin
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val layoutParams = view
                            .layoutParams as FrameLayout.LayoutParams
                        layoutParams.leftMargin =
                            Math.max(0, Math.min(mRootWidth - view.width, xScreenTouch - mXDelta))
                        layoutParams.topMargin =
                            Math.max(0, Math.min(mRootHeight - view.height, yScreenTouch - mYDelta))
                        view.layoutParams = layoutParams
                    }
                }
                true
            }

        editBind.editImgLyt.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        editBind.editImgLyt.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this)
                    }
                    mRootWidth = editBind.editImgLyt.getWidth()
                    mRootHeight = editBind.editImgLyt.getHeight()
                }
            })


        editBind.editBtnAddTxt.setOnClickListener {
//            showAddNameDialog()
            var txt = editBind.editGetTxt.text.toString()
            editBind.editGetTxt.setText("")
            when(addedTxt){
                0 ->{
                    createTextiew(txt)
                    addedTxt = 1
                }else -> Toast.makeText(this, "Please Set the text first in order to add another", Toast.LENGTH_SHORT).show()
            }

            editBind.editTxtCusView.visibility = View.VISIBLE
        }


        editBind.editChangeColor.setOnClickListener {
            changeTextColor()

        }

        editBind.editFontSizeSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {

                        addingTextView.setTextSize(fontSize)

            }

        })

        editBind.editFontSizeSlider.addOnChangeListener { slider, value, fromUser ->

                    addingTextView.setTextSize(value)
                    fontSize = value
        }

        editBind.editChangeFontSize.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                editBind.editFontSizeSlider.visibility = View.VISIBLE
            } else {
                editBind.editFontSizeSlider.visibility = View.GONE
            }
        }

        editBind.editFontBold.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                        addingTextView.setTypeface(null, Typeface.BOLD)

            } else {
                        addingTextView.setTypeface(null, Typeface.NORMAL)
            }
        }
        editBind.editFontItalic.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                        addingTextView.setTypeface(null, Typeface.ITALIC)

            } else {

                        addingTextView.setTypeface(null, Typeface.NORMAL)

            }
        }


        editBind.editBtnSet.setOnClickListener {
            val curSs = getScreenShotFromView(editBind.editImgLyt)
            if (curSs!=null){
                addedTxt = 0
                removeTextView()
                editBind.editImg.setImageBitmap(curSs)
            }
        }

        editBind.editDoneImg.setOnClickListener {
            val ss = getScreenShotFromView(editBind.editImgLyt)

            if (ss!=null){
                saveMediaToStorage(ss)
            }
            val ir = ss?.let { it1 -> getImageUri(this, it1) }
            Log.e("fffyu", ir.toString())
            val i = Intent(this@EditorActivity, ResultActivity::class.java)
            i.putExtra("isFromEdit", true)
            i.putExtra("FromEditorAct", ir)
            startActivity(i)
            addedTxt = 0


        }

    }



    fun createTextiew(txt: String) {
        val txtParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addingTextView.layoutParams = txtParams
        addingTextView.text = txt
        addingTextView.textSize = 20F
        addingTextView.setTextColor(Color.BLACK)
        addingTextView.setOnTouchListener(mOnTouchListener)
        editBind.editImgLyt.addView(addingTextView)
        val gf = addNameDialog.create()
        gf.dismiss()

    }

    fun removeTextView(){
        editBind.editImgLyt.removeView(addingTextView)
    }


    fun changeTextColor() {
        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerObserver() {
                override fun onColorPicked(color: Int) {
                    addingTextView.setTextColor(color)
                }

                fun onColor(color: Int, fromUser: Boolean) {}
            })
    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }


    // this method saves the image to gallery
    private fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Captured View and saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }


}


