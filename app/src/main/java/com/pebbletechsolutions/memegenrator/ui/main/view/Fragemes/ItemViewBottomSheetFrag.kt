package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.FragmentResultListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pebbletechsolutions.memegenrator.databinding.FragmentItemViewBottomSheetBinding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pebbletechsolutions.memegenrator.data.model.FavModel
import com.pebbletechsolutions.memegenrator.data.model.FavListViewModel
import com.pebbletechsolutions.memegenrator.ui.main.view.EditorActivity
import com.pebbletechsolutions.memegenrator.ui.main.view.ResultActivity
import com.pebbletechsolutions.memegenrator.utils.FavItemClickInterface
import com.theartofdev.edmodo.cropper.CropImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import android.app.ProgressDialog
import com.google.android.material.progressindicator.CircularProgressIndicator
import android.graphics.BitmapFactory

import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import android.app.DownloadManager
import android.os.Environment
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService





class ItemViewBottomSheetFrag : BottomSheetDialogFragment() {

    var ImgRs: String = ""
    private lateinit var favVM: FavListViewModel


    private var bottomSheetBind: FragmentItemViewBottomSheetBinding? = null
    private val bsBind get() = bottomSheetBind
    private lateinit var passMore: Bundle
    val fragFav = FavouriteFrag()
    private lateinit var myUri: Uri
    var mProgressDialog: CircularProgressIndicator? = null
    var favList: ArrayList<FavModel> = arrayListOf<FavModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favVM = ViewModelProvider(this).get(FavListViewModel::class.java)

//        requireActivity().supportFragmentManager.setFragmentResultListener("fromHomeFrag",
//            viewLifecycleOwner, { requestKey, result ->
//
//            })
        passMore = Bundle()


        parentFragmentManager.setFragmentResultListener("fromHomeFrag", this, FragmentResultListener { requestKey, result ->

            ImgRs = result.get("HomeFragList").toString()
            Log.e("immm", ImgRs.toString())
            myUri = Uri.parse(ImgRs)
            Log.e("immmm", myUri.toString())
//            bottomSheetBind!!.bsMemeImg.setImageResource(ImgRs)
            Glide.with(requireContext()).load(ImgRs).into(bottomSheetBind!!.bsMemeImg)

        })

        parentFragmentManager.setFragmentResultListener("FromFavFrag", this, FragmentResultListener { requestKey, result ->
            ImgRs = result.get("FavFragUri").toString()
            Glide.with(requireContext()).load(ImgRs).into(bottomSheetBind!!.bsMemeImg)
        })

        parentFragmentManager.setFragmentResultListener("SavedFragImg", this, FragmentResultListener { requestKey, result ->
            ImgRs = result.get("FromSaveFrag").toString()
            Glide.with(requireContext()).load(ImgRs).into(bottomSheetBind!!.bsMemeImg)
        })

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetBind = FragmentItemViewBottomSheetBinding.inflate(inflater, container, false)
        val view = bsBind?.root





        bottomSheetBind!!.bsBtnEdit.setOnClickListener {

           var inte = Intent(requireContext(), EditorActivity::class.java)
            inte.putExtra("homeBS", true)
            inte.putExtra("fromHomeBs", ImgRs)
            startActivity(inte)



        }


        bottomSheetBind!!.bsBtnShare.setOnClickListener {
            showShareIntent()
        }

        bottomSheetBind!!.bsBtnAddToFav.setOnClickListener {
//            favList.add(FavImageModel(ImgRs))
            favVM.insertFavrtImg(FavModel(ImgRs))
            Toast.makeText(requireContext(), "Added To Favourite", Toast.LENGTH_SHORT).show()
        }




        return view
    }

    companion object {
        const val TAG = "ItemBottomSheet"
        const val IMAGE_URL = "ADDED_FROM_BOTTOM_SHEET"
    }




    private fun showShareIntent() {
        // Step 1: Create Share itent
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")

        // Step 2: Get Bitmap from your imageView

        val bitmap = bottomSheetBind!!.bsMemeImg.drawable.toBitmap()// your imageView here.

        // Step 3: Compress image
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        // Step 4: Save image & get path of it
        val path = MediaStore.Images.Media.insertImage(requireActivity().contentResolver, bitmap, "tempimage", null)

        // Step 5: Get URI of saved image
        val uri = Uri.parse(path)

        // Step 6: Put Uri as extra to share intent
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // Step 7: Start/Launch Share intent
        startActivity(intent)
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

    private fun CreateTempFile(url: String){

        val localFile = File.createTempFile("Firebase", "jpg")


    }








}



