package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.FdbMemeModel
import com.pebbletechsolutions.memegenrator.data.model.MemeModel
import com.pebbletechsolutions.memegenrator.databinding.CustomHomeItemDialogBinding
import com.pebbletechsolutions.memegenrator.databinding.FragmentHomeBinding
import com.pebbletechsolutions.memegenrator.ui.main.adapter.HomeRecyclerAdapter
import com.pebbletechsolutions.memegenrator.ui.main.view.EditorActivity
import com.pebbletechsolutions.memegenrator.utils.OnBSBtnClick
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner
import com.pebbletechsolutions.memegenrator.utils.startAnimation
import kotlinx.coroutines.GlobalScope


class HomeFragment : Fragment(), OnRecyclerItemClickListner{

    private lateinit var dRef: DatabaseReference
    private lateinit var FMemeList: ArrayList<FdbMemeModel>

    private var _homeFragBind: FragmentHomeBinding?=null
    private var  DialogBind: CustomHomeItemDialogBinding? = null
    private val HomeBind get() = _homeFragBind
    var customDialog: MaterialAlertDialogBuilder? = null
    private lateinit var itemBottomSheet: ItemViewBottomSheetFrag
    val pass: Bundle = Bundle()

    var ImgUrl: String = ""


    private lateinit var ImgRef: DatabaseReference
    private lateinit var HomeRV: RecyclerView
    private lateinit var MemeListData: MutableList<MemeModel>






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _homeFragBind = FragmentHomeBinding.inflate(inflater, container, false)
        val view = HomeBind?.root

        itemBottomSheet = ItemViewBottomSheetFrag()

        _homeFragBind!!.shimmerPlaceHolder.startShimmer()

        customDialog = MaterialAlertDialogBuilder(requireContext())

        FMemeList = arrayListOf<FdbMemeModel>()


//        MemeListData = arrayListOf()
//        MemeListData.add(MemeModel(R.drawable.meme1))
//        MemeListData.add(MemeModel(R.drawable.meme2))
//        MemeListData.add(MemeModel(R.drawable.meme3))
//        MemeListData.add(MemeModel(R.drawable.meme4))
//        MemeListData.add(MemeModel(R.drawable.meme5))
//        MemeListData.add(MemeModel(R.drawable.meme6))
//        MemeListData.add(MemeModel(R.drawable.meme7))
//        MemeListData.add(MemeModel(R.drawable.meme8))
//        MemeListData.add(MemeModel(R.drawable.meme9))
//        MemeListData.add(MemeModel(R.drawable.meme10))
//        MemeListData.add(MemeModel(R.drawable.meme11))
//        MemeListData.add(MemeModel(R.drawable.meme12))
//        MemeListData.add(MemeModel(R.drawable.meme13))
//        MemeListData.add(MemeModel(R.drawable.meme14))
//        MemeListData.add(MemeModel(R.drawable.meme15))
//        MemeListData.add(MemeModel(R.drawable.meme16))
//        MemeListData.add(MemeModel(R.drawable.meme17))
//        MemeListData.add(MemeModel(R.drawable.meme18))
//        MemeListData.add(MemeModel(R.drawable.meme19))
//        MemeListData.add(MemeModel(R.drawable.meme20))
//        MemeListData.add(MemeModel(R.drawable.meme21))
//        MemeListData.add(MemeModel(R.drawable.meme22))
//        MemeListData.add(MemeModel(R.drawable.meme23))
//        MemeListData.add(MemeModel(R.drawable.meme24))
//        MemeListData.add(MemeModel(R.drawable.meme25))
//        MemeListData.add(MemeModel(R.drawable.meme26))
//        MemeListData.add(MemeModel(R.drawable.meme27))
//        MemeListData.add(MemeModel(R.drawable.meme28))
//        MemeListData.add(MemeModel(R.drawable.meme29))
//        MemeListData.add(MemeModel(R.drawable.meme30))
//        MemeListData.add(MemeModel(R.drawable.meme31))
//        MemeListData.add(MemeModel(R.drawable.meme32))
//        MemeListData.add(MemeModel(R.drawable.meme33))

        _homeFragBind!!.homeRV.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        _homeFragBind!!.homeRV.adapter = HomeRecyclerAdapter(MemeListData as ArrayList<MemeModel>, this)
        _homeFragBind!!.shimmerPlaceHolder.visibility = View.VISIBLE
        _homeFragBind!!.shimmerPlaceHolder.startShimmer()
        getMemeImageFromFirebase()
        return view
    }

    private fun getMemeImageFromFirebase() {

        dRef = FirebaseDatabase.getInstance().getReference("memeList")
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (memeSnapshot in snapshot.children){
                        val meme = memeSnapshot.getValue(FdbMemeModel::class.java)
                        FMemeList.add(meme!!)
                    }
                    _homeFragBind!!.shimmerPlaceHolder.stopShimmer()
                    _homeFragBind!!.shimmerPlaceHolder.visibility = View.GONE
                    _homeFragBind!!.homeRV.adapter = HomeRecyclerAdapter(FMemeList, this@HomeFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragBind = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onItemClick(position: Int) {
//        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
//        ShowCustomDialog(position)
        showBottomSheet(position)
    }



    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }




    fun showBottomSheet(position: Int){
        pass.putString("HomeFragList", FMemeList[position].image)
        ImgUrl = FMemeList[position].image.toString()
        parentFragmentManager.setFragmentResult("fromHomeFrag", pass)
        itemBottomSheet.show(requireActivity().supportFragmentManager, ItemViewBottomSheetFrag.TAG)
    }

}