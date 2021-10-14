package com.pebbletechsolutions.memegenrator.ui.main.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var editBind: ActivityEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBind = ActivityEditorBinding.inflate(layoutInflater)
        val view =  editBind!!.root
        setContentView(view)


        val bundle: Bundle? = intent.extras
        if (bundle!=null){
            if (bundle.getBoolean("taken")){
                val imgInt = bundle.get("TakenPicture")
                val bitmap: Bitmap = BitmapFactory.decodeFile(imgInt.toString())
                editBind.editImg.setImageBitmap(bitmap)
            }else {
                val imgUri = bundle.get("pickedImage")
                Log.e("Picked2", imgUri.toString())
                editBind.editImg.setImageURI(imgUri as Uri?)
            }
        }else{
            Toast.makeText(this, "No image Passed Or some erro ", Toast.LENGTH_LONG).show()
        }

        editBind.editBackImg.setOnClickListener {
            startActivity(Intent(this@EditorActivity, HomeActivity::class.java))
            finish()
        }

    }
}