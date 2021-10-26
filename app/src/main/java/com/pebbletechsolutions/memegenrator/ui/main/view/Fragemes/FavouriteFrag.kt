package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pebbletechsolutions.memegenrator.data.model.FavModel
import com.pebbletechsolutions.memegenrator.data.model.FavListViewModel
import com.pebbletechsolutions.memegenrator.databinding.FragmentFavouriteBinding
import com.pebbletechsolutions.memegenrator.ui.main.adapter.FavouriteRecyclerAdapter
import com.pebbletechsolutions.memegenrator.utils.FavItemClickInterface
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner


class FavouriteFrag : Fragment(), OnRecyclerItemClickListner, FavItemClickInterface {

    private lateinit var favVieModel: FavListViewModel
    private lateinit var itemBs: ItemViewBottomSheetFrag
    var favFragBind: FragmentFavouriteBinding? = null
    val favFB get() = favFragBind
    val favBundle = Bundle()
    var FavImg: String = ""


    var favListData: ArrayList<FavModel> = arrayListOf<FavModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemBs = ItemViewBottomSheetFrag()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favFragBind = FragmentFavouriteBinding.inflate(inflater, container, false)
        val view = favFB!!.root

        favFragBind!!.favFragRV.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FavouriteRecyclerAdapter(this, this)
        favFragBind!!.favFragRV.adapter = adapter

        favVieModel = ViewModelProvider(this).get(FavListViewModel::class.java)

        favVieModel.allImgs.observe(viewLifecycleOwner, Observer {list ->
            list?.let {
                favListData.addAll(list)
                adapter.updateList(it)
            }
        })


        return  view



    }

    override fun onItemClick(position: Int) {
        showFavBS(position)
    }

    override fun onItemLongClick(position: Int) {

    }


    fun showFavBS(position: Int){
        favBundle.putString("FavFragUri", favListData[position].FavImgUri)
        Log.e("favImgUri", favListData[position].FavImgUri)
        FavImg = favListData[position].FavImgUri
        parentFragmentManager.setFragmentResult("FromFavFrag", favBundle)
        itemBs.show(requireActivity().supportFragmentManager, ItemViewBottomSheetFrag.TAG)
    }

    override fun onFavItemClick(FavImg: FavModel) {
        favVieModel.deleteFavrtImg(FavImg)
        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
    }


}