package com.pebbletechsolutions.memegenrator.ui.main.view

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.pebbletechsolutions.memegenrator.data.model.FavListViewModel
import com.pebbletechsolutions.memegenrator.data.model.FavModel
import com.pebbletechsolutions.memegenrator.data.model.SavedModel
import com.pebbletechsolutions.memegenrator.databinding.ActivityResultBinding

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class ResultActivity : AppCompatActivity() {

    private var resultBind: ActivityResultBinding? =null
    private var rImgUri: Uri? = null
    private var curUri = ""
    var savedImgList: ArrayList<FavModel> = arrayListOf<FavModel>()
    private lateinit var favSavVM: FavListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBind = ActivityResultBinding.inflate(layoutInflater)
        val rView = resultBind?.root
        setContentView(rView)

        favSavVM = ViewModelProvider(this).get(FavListViewModel::class.java)

        val intent: Intent = intent
        if (intent.extras!!.get("isFromEdit") == true){
            val bb = intent.extras!!.get("FromEditorAct")
            curUri = bb.toString()
            Log.e("fffuri", bb.toString())
            resultBind!!.croppedImg.setImageURI(bb as Uri?)
        }

        if (intent.extras!!.get("FromCrop") == true){
            if (intent.extras!!.get("croppedOk")==true){
                val fromCCrop = intent.extras!!.get("croppedUri")
                Log.e("cropUri", fromCCrop.toString())
                curUri = fromCCrop.toString()
                resultBind!!.croppedImg.setImageURI(fromCCrop as Uri?)
            }else{
                var error = intent.extras!!.get("croppedUriError")
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }

        }


        resultBind!!.resultSaveImg.setOnClickListener {
            val rs = getScreenShotFromView(resultBind!!.croppedImg)
            if (rs != null){
                saveMediaToStorage(rs)
                rImgUri = getImageUri(this, rs)
                favSavVM.insertSVImg(SavedModel(rImgUri.toString()))
                Log.e("rUri", rImgUri.toString())
            }

        }



        resultBind!!.resultShareImg.setOnClickListener {
            showShareIntent()
        }

    }


    fun getFileExtension(uri: Uri?): String? {
        val cr: ContentResolver  = contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri!!))
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

    private fun showShareIntent() {
        // Step 1: Create Share itent
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")

        // Step 2: Get Bitmap from your imageView

        val bitmap = resultBind!!.croppedImg.drawable.toBitmap()// your imageView here.

        // Step 3: Compress image
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        // Step 4: Save image & get path of it
        val path = MediaStore.Images.Media.insertImage(this@ResultActivity.contentResolver, bitmap, "tempimage", null)

        // Step 5: Get URI of saved image
        val uri = Uri.parse(path)

        // Step 6: Put Uri as extra to share intent
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // Step 7: Start/Launch Share intent
        startActivity(intent)
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

    fun saveMediaToStorage(bitmap: Bitmap) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            applicationContext.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Image Saved In Pictures", Toast.LENGTH_SHORT).show()

        }
    }
}