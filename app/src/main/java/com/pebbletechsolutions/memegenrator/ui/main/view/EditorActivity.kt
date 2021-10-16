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
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.databinding.ActivityEditorBinding
import com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes.EditImageFrag

class EditorActivity : AppCompatActivity() {

    private lateinit var editBind: ActivityEditorBinding
    var bsImgUri = ""
    var passMoreBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBind = ActivityEditorBinding.inflate(layoutInflater)
        val view =  editBind!!.root
        setContentView(view)

        var intent = intent
        bsImgUri = intent.extras?.get("fromHomeBs") as String
        Log.e("im", bsImgUri)
        passMoreBundle.putString("SlctImgUri", bsImgUri)

        replaceFragment(EditImageFrag())

    }


    private fun replaceFragment(frag: Fragment) {
        frag.arguments = passMoreBundle
        val fragTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.replace(R.id.editFragContainer, frag)
        fragTransaction.commit()

    }


}