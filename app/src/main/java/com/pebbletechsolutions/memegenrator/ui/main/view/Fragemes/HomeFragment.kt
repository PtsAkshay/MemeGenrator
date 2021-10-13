package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.*
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.MemeModel
import com.pebbletechsolutions.memegenrator.databinding.FragmentHomeBinding
import com.pebbletechsolutions.memegenrator.ui.main.adapter.HomeRecyclerAdapter


class HomeFragment : Fragment() {

    private var _homeFragBind: FragmentHomeBinding?=null
    private val HomeBind get() = _homeFragBind


    private lateinit var ImgRef: DatabaseReference
    private lateinit var HomeRV: RecyclerView
    private lateinit var MemeList: ArrayList<MemeModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _homeFragBind = FragmentHomeBinding.inflate(inflater, container, false)
        val view = HomeBind?.root


        val MemeListData: MutableList<MemeModel> = arrayListOf()
        MemeListData.add(MemeModel(R.drawable.meme1))
        MemeListData.add(MemeModel(R.drawable.meme2))
        MemeListData.add(MemeModel(R.drawable.meme3))
        MemeListData.add(MemeModel(R.drawable.meme4))
        MemeListData.add(MemeModel(R.drawable.meme5))
        MemeListData.add(MemeModel(R.drawable.meme6))
        MemeListData.add(MemeModel(R.drawable.meme7))
        MemeListData.add(MemeModel(R.drawable.meme8))
        MemeListData.add(MemeModel(R.drawable.meme9))
        MemeListData.add(MemeModel(R.drawable.meme10))
        MemeListData.add(MemeModel(R.drawable.meme11))
        MemeListData.add(MemeModel(R.drawable.meme12))
        MemeListData.add(MemeModel(R.drawable.meme13))
        MemeListData.add(MemeModel(R.drawable.meme14))
        MemeListData.add(MemeModel(R.drawable.meme15))
        MemeListData.add(MemeModel(R.drawable.meme16))
        MemeListData.add(MemeModel(R.drawable.meme17))
        MemeListData.add(MemeModel(R.drawable.meme18))
        MemeListData.add(MemeModel(R.drawable.meme19))
        MemeListData.add(MemeModel(R.drawable.meme20))
        MemeListData.add(MemeModel(R.drawable.meme21))
        MemeListData.add(MemeModel(R.drawable.meme22))
        MemeListData.add(MemeModel(R.drawable.meme23))
        MemeListData.add(MemeModel(R.drawable.meme24))
        MemeListData.add(MemeModel(R.drawable.meme25))
        MemeListData.add(MemeModel(R.drawable.meme26))
        MemeListData.add(MemeModel(R.drawable.meme27))
        MemeListData.add(MemeModel(R.drawable.meme28))
        MemeListData.add(MemeModel(R.drawable.meme29))
        MemeListData.add(MemeModel(R.drawable.meme30))
        MemeListData.add(MemeModel(R.drawable.meme31))
        MemeListData.add(MemeModel(R.drawable.meme32))
        MemeListData.add(MemeModel(R.drawable.meme33))

        _homeFragBind!!.homeRV.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        _homeFragBind!!.homeRV.adapter = HomeRecyclerAdapter(MemeListData as ArrayList<MemeModel>)

        return view
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragBind = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}