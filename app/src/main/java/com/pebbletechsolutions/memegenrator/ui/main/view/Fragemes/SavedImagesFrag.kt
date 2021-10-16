package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pebbletechsolutions.memegenrator.data.model.FdbMemeModel
import com.pebbletechsolutions.memegenrator.databinding.CustomHomeItemDialogBinding
import com.pebbletechsolutions.memegenrator.databinding.FragmentSavedImagesBinding
import com.pebbletechsolutions.memegenrator.ui.main.view.EditorActivity
import java.io.File
import java.io.IOException


class SavedImagesFrag : Fragment() {


    private lateinit var savedRef: DatabaseReference
    private lateinit var savStoreRef: StorageReference
    private lateinit var savedImgList: ArrayList<FdbMemeModel>
    private var savImgUri: Uri? = null

    var customDialog: MaterialAlertDialogBuilder? = null
    private var DialogBind: CustomHomeItemDialogBinding? = null

    private var savFragBind: FragmentSavedImagesBinding? = null
    private val SBind get() = savFragBind
    var isPhototaken = false
    var cropingPhotpPath: String = ""
    var cropImgName: String = "croppedImage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedImgList = ArrayList()

        savedRef = FirebaseDatabase.getInstance().getReference("savedImgList")
        savStoreRef = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savFragBind = FragmentSavedImagesBinding.inflate(inflater, container, false)
        val view = SBind?.root

        if (savedImgList.isEmpty()){
            savFragBind!!.notItemLyt.isVisible = true
        }


        savFragBind!!.btnAddItem.setOnClickListener {

        }



        return view
    }


    fun takePictureFromCamera(){

        isPhototaken = true
        val storageDirectory: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        try {
            val imageFile: File = File.createTempFile(cropImgName, ".jpg", storageDirectory)
            val imgUri: Uri = FileProvider.getUriForFile(requireContext(), "com.pebbletechsolutions.memegenrator.fileprovider", imageFile)
            cropingPhotpPath = imageFile.absolutePath
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            i.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
//            takePhoto.launch(i)
            startActivityForResult(i, 111)

        }catch (e: IOException){

        }


    }

    fun pickImg(){
        isPhototaken = false
        val j =  Intent(Intent.ACTION_PICK)
        j.type = "image/*"
        startActivityForResult(j, 222)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK){
            if (requestCode == 111){
                val u: Intent = Intent(requireContext(), EditorActivity::class.java)
                u.putExtra("takenSavedImg", isPhototaken)
                u.putExtra("TakenPictureFromSavedImg", cropingPhotpPath)
                startActivity(u)
            }else if(requestCode == 222){
                val pickUri: Uri = data?.data!!
                Log.e("Picked", pickUri.toString())
                val k = Intent(requireContext(), EditorActivity::class.java)
                k.putExtra("pickedSavedImg", isPhototaken)
                k.putExtra("pickedImageFromSavedImg", pickUri)
                startActivity(k)
            }

        }
    }

}



