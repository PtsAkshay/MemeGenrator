package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.data.model.MemeModel
import com.pebbletechsolutions.memegenrator.data.model.SavedImageModel
import com.pebbletechsolutions.memegenrator.databinding.CustomHomeItemDialogBinding
import com.pebbletechsolutions.memegenrator.databinding.FragmentSavedImagesBinding
import com.pebbletechsolutions.memegenrator.ui.main.view.EditorActivity
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner


class SavedImagesFrag : Fragment(), OnRecyclerItemClickListner {

    private lateinit var SaveListData: MutableList<SavedImageModel>
    var customDialog: MaterialAlertDialogBuilder? = null
    private var  DialogBind: CustomHomeItemDialogBinding? = null

    private var savBind: FragmentSavedImagesBinding?=null
    private val SBind get() = savBind



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        savBind = FragmentSavedImagesBinding.inflate(inflater, container, false)
        val view = SBind?.root

        customDialog = MaterialAlertDialogBuilder(requireContext())

        return view
    }

    override fun onItemClick(position: Int) {
       ShowCustomDialog(position)
    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }


    private fun ShowCustomDialog(position: Int) {
        DialogBind = CustomHomeItemDialogBinding.inflate(layoutInflater)
        val DialogView = DialogBind!!.root
        customDialog!!.setView(DialogView)
        DialogBind!!.dialogMemeImg.setImageResource(SaveListData[position].SavImg)
        customDialog!!.show()
        val df = customDialog!!.create()
        DialogBind!!.dialogBtnEdit.setOnClickListener {
            Toast.makeText(context, "Edit Button Clicked", Toast.LENGTH_SHORT).show()
            val i: Intent = Intent(context, EditorActivity::class.java)
            i.putExtra("ClickedImg", SaveListData[position].SavImg)
            startActivity(i)
            df.dismiss()
        }

    }



}