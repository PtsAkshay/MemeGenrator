package com.pebbletechsolutions.memegenrator.ui.main.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pebbletechsolutions.memegenrator.databinding.ActivityEditorBinding
import com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes.EditImageFrag


import android.app.ProgressDialog

import android.widget.ImageView

import android.widget.RelativeLayout

import android.widget.TextView
import com.pebbletechsolutions.memegenrator.utils.RotationGestureDetector
import android.util.TypedValue
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.content.DialogInterface

import android.R
import android.graphics.Color

import android.provider.MediaStore
import android.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import top.defaults.colorpicker.ColorPickerView


class EditorActivity : AppCompatActivity(), RotationGestureDetector.OnRotationGestureListener {

    private lateinit var editBind: ActivityEditorBinding
    var bsImgUri = ""
    var passMoreBundle = Bundle()


    var IMAGE_IN_URI = "imageInURI"
    var TEXT_TO_WRITE = "sourceText"
    var TEXT_FONT_SIZE = "textFontSize"
    var TEXT_COLOR = "textColor"
    var IMAGE_OUT_URI = "imageOutURI"
    var IMAGE_OUT_ERROR = "imageOutError"

    var TEXT_ON_IMAGE_RESULT_OK_CODE = 1
    var TEXT_ON_IMAGE_RESULT_FAILED_CODE = -1
    var TEXT_ON_IMAGE_REQUEST_CODE = 4


    private val TAG: String = EditorActivity::class.java.getSimpleName()
    private val imageInUri: Uri? = null
    private  var imageOutUri:android.net.Uri? = null
    private val saveDir = "/tmp/"
    private val textToWrite = ""
    private  var textColor:kotlin.String? = "#ffffff"
    private val textFontSize = 0f
    var addTextView: TextView? = null
    private var errorAny = ""
    private var sourceImageView: ImageView? = null
    private var workingLayout: RelativeLayout? = null
    private  var baseLayout:RelativeLayout? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var progressDialog: ProgressDialog? = null
    private var mRotationGestureDetector: RotationGestureDetector? = null
    private lateinit var colorPickerView: ColorPickerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBind = ActivityEditorBinding.inflate(layoutInflater)
        val view = editBind.root
        setContentView(view)


        addTextView = TextView(this)
        colorPickerView = ColorPickerView(this)
        scaleGestureDetector = ScaleGestureDetector(this@EditorActivity, simpleOnScaleGestureListener())
        mRotationGestureDetector = RotationGestureDetector(this@EditorActivity)
        uiSetup();

        editBind.editDoneImg.setOnClickListener {
            progressDialog!!.setMessage("Adding Text")
            progressDialog!!.show()

            //set the text
            val doneSetting = setTextFinal()
            if (doneSetting) {
                val intent = Intent()
                intent.putExtra(IMAGE_OUT_URI, imageOutUri.toString())
                setResult(TEXT_ON_IMAGE_RESULT_OK_CODE, intent)
                if (progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                finish()
            } else {
                val intent = Intent()
                intent.putExtra(IMAGE_OUT_ERROR, errorAny)
                setResult(TEXT_ON_IMAGE_RESULT_FAILED_CODE, intent)
                if (progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                finish()
            }
        }


        editBind.editBtnAddImg.setOnClickListener {
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
                        colorPickerView.setBackgroundColor(color)
                    }

                    fun onColor(color: Int, fromUser: Boolean) {}
                })
        }


            var intent = intent
            bsImgUri = intent.extras?.get("fromHomeBs") as String
            Log.e("im", bsImgUri)
//        passMoreBundle.putString("SlctImgUri", bsImgUri)

            Glide.with(this).load(bsImgUri).into(editBind.editImg)

//        replaceFragment(EditImageFrag())

        }

        override fun OnRotation(rotationDetector: RotationGestureDetector?) {
            val angle: Float = rotationDetector!!.angle
            addTextView!!.rotation = angle
        }


        class simpleOnScaleGestureListener : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                //change the font size of the text on pinch
                var size: Float = EditorActivity().addTextView!!.getTextSize()
                val factor = detector.scaleFactor
                val product = size * factor
                EditorActivity().addTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, product)
                size = EditorActivity().addTextView!!.getTextSize()
                return true
            }
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            scaleGestureDetector!!.onTouchEvent(event)
            mRotationGestureDetector!!.onTouchEvent(event!!)
            return super.onTouchEvent(event)
        }


//    private fun replaceFragment(frag: Fragment) {
//        frag.arguments = passMoreBundle
//        val fragTransaction = supportFragmentManager.beginTransaction()
//        fragTransaction.replace(R.id.editFragContainer, frag)
//        fragTransaction.commit()
//
//    }

        private fun uiSetup() {
            //show progress dialog
            progressDialog = ProgressDialog(this@EditorActivity)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setMessage("Loading...")
            progressDialog!!.show()

            try {
                //get the image bitmap
                val bitmapForImageView =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imageInUri)
                var width = 0
                var height = 0
                //resize the views as per the image size
                if (bitmapForImageView.width > bitmapForImageView.height) {
                    width = 1280
                    height = 720
                } else if (bitmapForImageView.width < bitmapForImageView.height) {
                    width = 720
                    height = 1280
                } else {
                    width = 600
                    height = 600
                }

                //create the layouts
                //base layout
                baseLayout = RelativeLayout(this@EditorActivity)
                val baseLayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                baseLayout!!.setBackgroundColor(Color.parseColor("#000000"))

                //working layout
                workingLayout = RelativeLayout(this@EditorActivity)
                val workingLayoutParams = RelativeLayout.LayoutParams(width, height)
                workingLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                workingLayout!!.setLayoutParams(workingLayoutParams)


                //textview
                addTextView = TextView(this@EditorActivity)
                val textViewParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                addTextView!!.layoutParams = textViewParams

                //add views to working layout
                workingLayout!!.addView(sourceImageView)
                workingLayout!!.addView(addTextView)

                //add view to base layout
                baseLayout!!.addView(workingLayout)

                //set content view
                setContentView(baseLayout, baseLayoutParams)
                sourceImageView!!.setImageBitmap(bitmapForImageView)
                workingLayout!!.setDrawingCacheEnabled(true)
                if (progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss()
                }
            } catch (e: IOException) {
                if (progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss()
                }
                e.printStackTrace()
            }

            //setup the text view
            addTextView!!.text = textToWrite
            addTextView!!.textSize = textFontSize
            addTextView!!.setTextColor(Color.parseColor(textColor))
            addTextView!!.setOnTouchListener(object : View.OnTouchListener {
                var lastX = 0f
                var lastY = 0f
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    when (p1!!.action) {
                        MotionEvent.ACTION_DOWN -> {
                            lastX = p1!!.x
                            lastY = p1!!.y
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val dx = p1!!.x - lastX
                            val dy = p1!!.y - lastY
                            val finalX: Float = p0!!.getX() + dx
                            val finalY: Float = p0!!.getY() + dy + p0!!.getHeight()
                            p0!!.setX(finalX)
                            p0!!.setY(finalY)
                        }
                    }
                    return true
                }

            })


        }

        fun setTextFinal(): Boolean {
            addTextView!!.setOnTouchListener(null)
            var toBeReturn = false
            workingLayout!!.buildDrawingCache()
            toBeReturn = saveFile(Bitmap.createBitmap(workingLayout!!.drawingCache), "temp.jpg")
            return toBeReturn
        }

        fun saveFile(sourceImageBitmap: Bitmap, fileName: String): Boolean {
            var result = false
            val path = applicationInfo.dataDir + saveDir
            val pathFile = File(path)
            pathFile.mkdirs()
            val imageFile = File(path, fileName)
            if (imageFile.exists()) {
                imageFile.delete()
            }
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(imageFile)
                sourceImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                result = true
            } catch (e: Exception) {
                errorAny = e.message!!
                result = false
                e.printStackTrace()
            }
            imageOutUri = Uri.fromFile(imageFile)
            return result
        }


}