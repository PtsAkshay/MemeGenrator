package com.pebbletechsolutions.memegenrator.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.pebbletechsolutions.memegenrator.ui.main.view.EditorActivity
import kotlinx.android.synthetic.main.home_recycler_item.*
import java.io.File
import java.io.IOException
import java.util.*

class PickOrCaptureFile: AppCompatActivity() {

    val fileName: String = "memePhoto"
    var currentPhotoPath: String = ""

    private lateinit var takePhoto: ActivityResultLauncher<Intent>


    fun takePictureFromCamera(context: Context){


        val storageDirectory: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        try {
            val imageFile: File = File.createTempFile(fileName, ".jpg", storageDirectory)
            val imgUri: Uri = FileProvider.getUriForFile(context, "com.pebbletechsolutions.memegenrator.fileprovider", imageFile)
            currentPhotoPath = imageFile.absolutePath
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            i.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
//            takePhoto.launch(i)
            startActivityForResult(i, 11)

        }catch (e: IOException){

        }


    }




}