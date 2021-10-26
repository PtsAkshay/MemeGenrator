package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pebbletechsolutions.memegenrator.databinding.FragmentItemViewBottomSheetBinding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pebbletechsolutions.memegenrator.data.model.FavModel
import com.pebbletechsolutions.memegenrator.data.model.FavListViewModel
import com.pebbletechsolutions.memegenrator.ui.main.view.EditorActivity


class ItemViewBottomSheetFrag : BottomSheetDialogFragment() {

    var ImgRs: String = ""
    private lateinit var favVM: FavListViewModel

    private var bottomSheetBind: FragmentItemViewBottomSheetBinding? = null
    private val bsBind get() = bottomSheetBind
    private lateinit var passMore: Bundle
    val fragFav = FavouriteFrag()
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
//            bottomSheetBind!!.bsMemeImg.setImageResource(ImgRs)
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
            inte.putExtra("fromHomeBs", ImgRs)
            startActivity(inte)


        }

        bottomSheetBind!!.bsBtnAddToFav.setOnClickListener {
//            favList.add(FavImageModel(ImgRs))
            favVM.insertFavrtImg(FavModel(ImgRs))
        }





        return view
    }

    companion object {
        const val TAG = "ItemBottomSheet"
        const val IMAGE_URL = "ADDED_FROM_BOTTOM_SHEET"
    }





}



