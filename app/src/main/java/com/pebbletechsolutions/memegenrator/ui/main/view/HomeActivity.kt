package com.pebbletechsolutions.memegenrator.ui.main.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.databinding.ActivityMainBinding
import com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes.FavouriteFrag
import com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_recycler_item.*
import androidx.core.widget.NestedScrollView
import com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes.SavedImagesFrag
import com.pebbletechsolutions.memegenrator.utils.Permissions
import com.pebbletechsolutions.memegenrator.utils.PickOrCaptureFile
import com.pebbletechsolutions.memegenrator.utils.startAnimation
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.io.IOException


class HomeActivity : AppCompatActivity() {

    private lateinit var HABind: ActivityMainBinding
    var isFabOpen = false
    private lateinit var PERMISSIONS: Array<String>
    private lateinit var permissionUtils: Permissions
    private lateinit var takeOrCapture: PickOrCaptureFile
    val fileName: String = "memePhoto"
    var currentPhotoPath: String = ""
    var isPhototaken = false
    val TAKE_PICTURE_CODE = 11
    val PICK_IMAGE_CODE = 22

    private lateinit var takePhoto: ActivityResultLauncher<Intent>

    private val fabClose: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_close) }
    private val fabOpen: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_open) }
    private val fabExplose: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_explosive).apply {
        duration = 400
        interpolator = AccelerateDecelerateInterpolator()
    } }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        HABind = ActivityMainBinding.inflate(layoutInflater)
        val view = HABind.root
        setContentView(view)
        replaceFragment(HomeFragment())

        permissionUtils = Permissions()
        takeOrCapture = PickOrCaptureFile()


        PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
        )

//        takePhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
//            if (it.resultCode == RESULT_OK && it.data != null){
////               val bitmap: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
//                val u: Intent = Intent(this@HomeActivity, EditorActivity::class.java)
//                u.putExtra("TakenPicture", currentPhotoPath)
//                startActivity(u)
//            }
//
//        })

        HABind.homeBtnNav.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.btmNavHome -> {
                    replaceFragment(HomeFragment())
                    HABind.homeCreateFab.visibility = View.VISIBLE
                }
                R.id.btmNavSavedImg -> {
                    replaceFragment(SavedImagesFrag())
                }

                R.id.btnNavFav -> {
                    replaceFragment(FavouriteFrag())
                }
            }
            return@setOnItemSelectedListener true
        }

        HABind.homeCreateFab.setOnClickListener {
            animateFab()

        }

        HABind.homeTakePhotoFab.setOnClickListener {
            if (!permissionUtils.hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
                takePictureFromCamera()
                animateFab()
            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        HABind.homeSlctFileFab.setOnClickListener {
            if (!permissionUtils.hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
                pickImg()
                animateFab()
            }
        }

        HABind.homeNstdSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY -> // the delay of the extension of the FAB is set for 12 items
            if (scrollY > oldScrollY + 12 && HABind.homeCreateFab.isExtended) {
                HABind.homeCreateFab.shrink()

            }


            if (scrollY < oldScrollY - 12 && !HABind.homeCreateFab.isExtended) {
                HABind.homeCreateFab.extend()
            }


            if (scrollY == 0) {
                HABind.homeCreateFab.extend()
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.cropImgOption -> {
                CropImage.activity().start(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun animateFab()
    {
        if (isFabOpen){
            HABind.homeSlctFileFab.isClickable = false
            HABind.homeSlctFileFab.startAnimation(fabClose)
            HABind.homeSlctFileFab.visibility = View.GONE
            HABind.homeTakePhotoFab.isClickable = false
            HABind.homeTakePhotoFab.startAnimation(fabClose)
            HABind.homeTakePhotoFab.visibility = View.GONE
            HABind.blurView.visibility = View.INVISIBLE
            HABind.blutView2.visibility = View.GONE
            isFabOpen = false
        }else{
            HABind.homeSlctFileFab.isClickable = true
            HABind.homeSlctFileFab.startAnimation(fabOpen)
            HABind.homeSlctFileFab.visibility = View.VISIBLE
            HABind.homeTakePhotoFab.isClickable = true
            HABind.homeTakePhotoFab.startAnimation(fabOpen)
            HABind.homeTakePhotoFab.visibility = View.VISIBLE
            HABind.blurView.visibility = View.VISIBLE
            HABind.blurView.startAnimation(fabExplose){
                HABind.blutView2.visibility = View.VISIBLE
                HABind.blurView.visibility = View.INVISIBLE
            }
            isFabOpen = true
        }

    }

    private fun replaceFragment(frag: Fragment) {
        val fragTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.replace(R.id.FragContainer, frag)
        fragTransaction.isAddToBackStackAllowed
        fragTransaction.commit()

    }

    fun takePictureFromCamera(){

        isPhototaken = true
        val storageDirectory: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        try {
            val imageFile: File = File.createTempFile(fileName, ".jpg", storageDirectory)
            val imgUri: Uri = FileProvider.getUriForFile(this, "com.pebbletechsolutions.memegenrator.fileprovider", imageFile)
            currentPhotoPath = imageFile.absolutePath
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            i.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
//            takePhoto.launch(i)
            startActivityForResult(i, TAKE_PICTURE_CODE)

        }catch (e: IOException){

        }


    }

    fun pickImg(){
        isPhototaken = false
        val j =  Intent(Intent.ACTION_PICK)
        j.type = "image/*"
        startActivityForResult(j, PICK_IMAGE_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            if (requestCode == TAKE_PICTURE_CODE){
                val u: Intent = Intent(this, EditorActivity::class.java)
                u.putExtra("taken", isPhototaken)
                u.putExtra("TakenPicture", currentPhotoPath)
                startActivity(u)
            }else if(requestCode == PICK_IMAGE_CODE){
                val pickUri: Uri = data?.data!!
                Log.e("Picked", pickUri.toString())
                val k = Intent(this@HomeActivity, EditorActivity::class.java)
                k.putExtra("picked", isPhototaken)
                k.putExtra("pickedImage", pickUri)
                startActivity(k)
            }

        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val cropR: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK){
                val cropImgUri = cropR.uri
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("FromCrop", true)
                intent.putExtra("croppedOk", true)
                intent.putExtra("croppedUri", cropImgUri)
                startActivity(intent)
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val erro = cropR.error
                val j = Intent(this, ResultActivity::class.java)
                j.putExtra("FromCrop", true)
                j.putExtra("croppedOk", false)
                j.putExtra("croppedUriError", erro)
                startActivity(j)
            }
        }
    }



}