package com.pebbletechsolutions.memegenrator.ui.main.view

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.FdbMemeModel
import com.pebbletechsolutions.memegenrator.databinding.ActivityResultBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class ResultActivity : AppCompatActivity() {

    private var resultBind: ActivityResultBinding? =null
    private var savLName = ""
    private var curUri = ""
    private lateinit var savImgRef: DatabaseReference
    private lateinit var savRealTimeImgRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBind = ActivityResultBinding.inflate(layoutInflater)
        val rView = resultBind?.root
        setContentView(rView)

        val intent: Intent = intent
        if (intent.extras!!.get("isFromEdit") == true){
            val bb = intent.extras!!.get("FromEditorAct")
            curUri = bb.toString()
            Log.e("fffuri", bb.toString())
            resultBind!!.cropSavImgBtn.visibility = View.GONE
            resultBind!!.croppedImg.setImageURI(bb as Uri?)
        }

        if (intent.extras!!.get("FromCrop") == true){
            if (intent.extras!!.get("FCC")==true){
                val fromCCrop = intent.extras!!.get("cropedImg")
                curUri = fromCCrop.toString()
                savLName = intent.extras!!.get("savListName").toString()
                resultBind!!.croppedImg.setImageURI(fromCCrop as Uri?)
            }else if(intent.extras!!.get("FCC") == false){
                val takenCC = intent.extras!!.get("takenCrop")
                curUri = takenCC.toString()
                savLName = intent.extras!!.get("savListName").toString()
                resultBind!!.croppedImg.setImageURI(takenCC as Uri?)
            }

        }

        savImgRef = FirebaseDatabase.getInstance().getReference(savLName)
        savRealTimeImgRef = FirebaseStorage.getInstance().reference

        resultBind!!.cropSavAndShareImg.setOnClickListener {
            showShareIntent()
        }

        resultBind!!.cropSavAndUploadBtn.setOnClickListener {
            uploadToFirebase(curUri as Uri)
        }








    }

    fun uploadToFirebase(uri: Uri?){
        val fileRef: StorageReference =
            savRealTimeImgRef.child(System.currentTimeMillis().toString() + " " + getFileExtension(uri))
        fileRef.putFile(uri!!).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                var rModel: FdbMemeModel = FdbMemeModel(uri.toString())
                var rModelId = savImgRef.push().key
                savImgRef.child(rModelId!!).setValue(rModel)
                Toast.makeText(this@ResultActivity, "Upload Success", Toast.LENGTH_SHORT).show()
            }

        }.addOnProgressListener {
            resultBind!!.rbv.visibility = View.VISIBLE
            resultBind!!.rsltPB.visibility = View.VISIBLE
        }.addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: Exception) {
                resultBind!!.rbv.visibility = View.GONE
                resultBind!!.rsltPB.visibility = View.GONE
                Toast.makeText(this@ResultActivity, "Upload Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }


    fun getFileExtension(uri: Uri?): String? {
        val cr: ContentResolver  = contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri!!))
    }

    private fun showShareIntent() {
        // Step 1: Create Share itent
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")

        // Step 2: Get Bitmap from your imageView
        val bitmap = resultBind!!.croppedImg.drawable.toBitmap() // your imageView here.

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

        }
    }
}