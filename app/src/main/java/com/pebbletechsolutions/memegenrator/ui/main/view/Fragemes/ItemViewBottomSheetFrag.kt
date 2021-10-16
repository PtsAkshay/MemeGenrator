package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.databinding.FragmentItemViewBottomSheetBinding
import androidx.annotation.NonNull
import com.bumptech.glide.Glide


class ItemViewBottomSheetFrag : BottomSheetDialogFragment() {

    private var bottomSheetBind: FragmentItemViewBottomSheetBinding? = null
    private val bsBind get() = bottomSheetBind

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        requireActivity().supportFragmentManager.setFragmentResultListener("fromHomeFrag",
//            viewLifecycleOwner, { requestKey, result ->
//
//            })

        parentFragmentManager.setFragmentResultListener("fromHomeFrag", this, FragmentResultListener { requestKey, result ->

            Glide.with(requireContext()).load(result.get("HomeFragList")).into(bottomSheetBind!!.bsMemeImg)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetBind = FragmentItemViewBottomSheetBinding.inflate(inflater, container, false)
        val view = bsBind?.root



        return view
    }

    companion object {
        const val TAG = "ItemBottomSheet"
    }


}