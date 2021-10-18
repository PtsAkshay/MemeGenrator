package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.FragmentResultListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.databinding.FragmentItemViewBottomSheetBinding
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.pebbletechsolutions.memegenrator.ui.main.view.EditorActivity
import com.pebbletechsolutions.memegenrator.ui.main.view.ResultActivity
import com.pebbletechsolutions.memegenrator.utils.bottomSheetButtonClick
import com.theartofdev.edmodo.cropper.CropImage


class ItemViewBottomSheetFrag : BottomSheetDialogFragment() {








    private var bottomSheetBind: FragmentItemViewBottomSheetBinding? = null
    private val bsBind get() = bottomSheetBind
    var ImgUri: String = ""
    private lateinit var passMore: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



//        requireActivity().supportFragmentManager.setFragmentResultListener("fromHomeFrag",
//            viewLifecycleOwner, { requestKey, result ->
//
//            })
        passMore = Bundle()



        parentFragmentManager.setFragmentResultListener("fromHomeFrag", this, FragmentResultListener { requestKey, result ->

            ImgUri = result.get("HomeFragList").toString()
            Log.e("immm", ImgUri)
            Glide.with(requireContext()).load(ImgUri).into(bottomSheetBind!!.bsMemeImg)
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
            inte.putExtra("fromHomeBs", ImgUri)
            startActivity(inte)


        }




        return view
    }

    companion object {
        const val TAG = "ItemBottomSheet"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }




}



