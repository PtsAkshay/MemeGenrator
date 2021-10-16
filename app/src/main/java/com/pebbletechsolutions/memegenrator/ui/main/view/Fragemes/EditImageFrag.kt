package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.Matrix

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.core.content.FileProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pebbletechsolutions.memegenrator.databinding.FragmentEditImageBinding
import com.pebbletechsolutions.memegenrator.databinding.SavedImgPickDialogBinding
import java.io.File
import java.io.IOException
import com.pebbletechsolutions.memegenrator.databinding.GetNameDialogLytBinding

import android.view.View.OnTouchListener

import androidx.fragment.app.FragmentResultListener
import com.bumptech.glide.Glide
import android.os.Build
import android.view.*

import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*


class EditImageFrag : Fragment() {

    private var mXDelta = 0
    private var mYDelta = 0
    private var mRootWidth = 0
    private var mRootHeight = 0



    private var editFragBind: FragmentEditImageBinding? = null
    private val editFBind get() = editFragBind
    private var savImgDialogBind: SavedImgPickDialogBinding? = null
    private var editSecImgName = "editAnotherImage"
    private var editSecImgPath = ""
    private var addText: String = ""
    private var xDelta: Int = 0
    private var yDelta: Int = 0
    private lateinit var touchListener: OnTouchListener
    private var gatedUri: String = ""
    private lateinit var mOnTouchListener: View.OnTouchListener



    private lateinit var savImgPickDialog: MaterialAlertDialogBuilder
    private lateinit var addNameDialog: MaterialAlertDialogBuilder
    private var nameDialogBind: GetNameDialogLytBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savImgPickDialog = MaterialAlertDialogBuilder(requireContext())
        addNameDialog = MaterialAlertDialogBuilder(requireContext())


        mOnTouchListener =
            OnTouchListener { view, event ->
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


        parentFragmentManager.setFragmentResultListener(
            "fromBs",
            this,
            FragmentResultListener { requestKey, result ->

                gatedUri = result.get("fromHomeSheet").toString()
                Log.e("immmm", gatedUri)
                Glide.with(requireContext()).load(gatedUri).into(editFragBind!!.editImg)
            })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editFragBind = FragmentEditImageBinding.inflate(inflater, container, false)
        val view = editFBind?.root

        if (arguments != null) {
            gatedUri = requireArguments().getString("SlctImgUri").toString()
            Log.e("gated", gatedUri)
            Glide.with(requireContext()).load(gatedUri).into(editFragBind!!.editImg)
        }


        editFragBind!!.editBtnAddTxt.setOnClickListener {
            showAddNameDialog()

        }

        editFragBind!!.editBtnAddImg.setOnClickListener {
            showPickDialog()

        }

        editFragBind!!.editImgLyt.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        editFragBind!!.editImgLyt.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this)
                    }
                    mRootWidth = editFragBind!!.editImgLyt.getWidth()
                    mRootHeight = editFragBind!!.editImgLyt.getHeight()
                }
            })




        return view
    }



    fun createTextiew(txt: String) {
        val addingText: TextView = TextView(requireContext())
        val txtParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addingText.layoutParams = txtParams
        addingText.text = txt
        addingText.textSize = 20F
        addingText.setTextColor(Color.BLACK)
        addingText.setOnTouchListener(mOnTouchListener)
        editFragBind!!.editImgLyt.addView(addingText)
        val gf = addNameDialog.create()
        gf.dismiss()

    }

    fun createImageView(Ir: Uri) {
        val addedImg = ImageView(requireContext())
        val imgParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addedImg.layoutParams = imgParams
        addedImg.setImageURI(Ir)
        editFragBind!!.editImgLyt.addView(addedImg)
        addedImg.setOnTouchListener(mOnTouchListener)

    }


    fun showPickDialog() {
        savImgDialogBind = SavedImgPickDialogBinding.inflate(layoutInflater)
        val dView = savImgDialogBind?.root
        savImgPickDialog.setView(dView)
        val sf = savImgPickDialog.create()
        savImgPickDialog.show()
        savImgDialogBind!!.svedImgTakePic.setOnClickListener {
            takePictureFromCamera()
            sf.dismiss()
        }
        savImgDialogBind!!.savImgSlctFile.setOnClickListener {
            pickImg()
            sf.dismiss()
        }
    }

    fun showAddNameDialog() {
        nameDialogBind = GetNameDialogLytBinding.inflate(layoutInflater)
        val adView = nameDialogBind?.root
        addNameDialog.setView(adView)
        val af = addNameDialog.create()
        addNameDialog.show()
        nameDialogBind!!.btnOk.setOnClickListener {
            addText = nameDialogBind!!.editTxtgetName.text.toString()
            createTextiew(addText)

        }
        nameDialogBind!!.btnCancle.setOnClickListener {
            af.dismiss()
        }
    }

    fun takePictureFromCamera() {

//        isPhototaken = true
        val storageDirectory: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        try {
            val imageFile: File = File.createTempFile(editSecImgName, ".jpg", storageDirectory)
            val imgUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.pebbletechsolutions.memegenrator.fileprovider",
                imageFile
            )
            editSecImgPath = imageFile.absolutePath
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            i.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
//            takePhoto.launch(i)
            startActivityForResult(i, 111)

        } catch (e: IOException) {

        }


    }

    fun pickImg() {
//        isPhototaken = false
        val j = Intent(Intent.ACTION_PICK)
        j.type = "image/*"
        startActivityForResult(j, 222)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 111) {
                val secImgUri = data?.data
                if (secImgUri != null) {
                    createImageView(secImgUri)
                }
            }
            if (requestCode == 222) {
                val secPImgUri = data?.data
                if (secPImgUri != null) {
                    createImageView(secPImgUri)
                }
            }
        }
    }

}

