package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.FavListViewModel
import com.pebbletechsolutions.memegenrator.data.model.SavedModel
import com.pebbletechsolutions.memegenrator.databinding.FragmentSavedImagesBinding
import com.pebbletechsolutions.memegenrator.ui.main.adapter.SavedImageRecyclerAdapter
import com.pebbletechsolutions.memegenrator.ui.main.view.ResultActivity
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner
import com.theartofdev.edmodo.cropper.CropImage


class SavedImagesFrag : Fragment(), OnRecyclerItemClickListner {

    private var savedBind: FragmentSavedImagesBinding? =null
    private val SBind get() = savedBind
    private lateinit var favAndSavViewModel: FavListViewModel
    private lateinit var bottmSheet: ItemViewBottomSheetFrag
    val savBundle = Bundle()
    var savedImgUri = ""
    var savedList: ArrayList<SavedModel> = arrayListOf<SavedModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        bottmSheet = ItemViewBottomSheetFrag()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedBind = FragmentSavedImagesBinding.inflate(inflater, container, false)
        val view = SBind?.root

        savedBind!!.savedFragRV.layoutManager = LinearLayoutManager(requireContext())
        val savedAdapter = SavedImageRecyclerAdapter(this)
        savedBind!!.savedFragRV.adapter = savedAdapter

        favAndSavViewModel = ViewModelProvider(this).get(FavListViewModel::class.java)

        favAndSavViewModel.allSavedImgs.observe(viewLifecycleOwner, Observer { SavList ->
            SavList?.let {
                if (it.isEmpty()){
                    savedBind!!.notItemLyt.visibility = View.VISIBLE
                }else{
                    savedList.addAll(SavList)
                    savedBind!!.notItemLyt.visibility = View.GONE
                    savedAdapter.updateSavedImgList(it)
                }


            }

        })

        savedBind!!.btnAddItem.setOnClickListener {
            CropImage.activity().start(requireActivity(), this)
        }



        return  view
    }

    override fun onItemClick(position: Int) {
        ShowSavedBs(position)
    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }

    fun ShowSavedBs(position: Int){
        savBundle.putString("FromSaveFrag", savedList[position].SavImgUri)
        Log.e("favImgUri", savedList[position].SavImgUri)
        savedImgUri = savedList[position].SavImgUri
        parentFragmentManager.setFragmentResult("SavedFragImg", savBundle)
        bottmSheet.show(requireActivity().supportFragmentManager, ItemViewBottomSheetFrag.TAG)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val cropResult: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK){
                val cropUri = cropResult.uri
                Log.e("crop", cropUri.toString())
                val ri: Intent = Intent(requireContext(), ResultActivity::class.java)
                ri.putExtra("FromCrop", true)
                ri.putExtra("croppedOk", true)
                ri.putExtra("croppedUri", cropUri)
                startActivity(ri)
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error: Exception = cropResult.error
                val cr: Intent = Intent(requireContext(), ResultActivity::class.java)
                cr.putExtra("FromCrop", true)
                cr.putExtra("croppedOk", false)
                cr.putExtra("croppedUriError", error)
                startActivity(cr)
            }
        }
    }

}