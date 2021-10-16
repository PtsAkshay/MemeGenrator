package com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pebbletechsolutions.memegenrator.data.model.FdbMemeModel
import com.pebbletechsolutions.memegenrator.databinding.CustomHomeItemDialogBinding
import com.pebbletechsolutions.memegenrator.databinding.FragmentSavedImagesBinding
import com.pebbletechsolutions.memegenrator.databinding.SavedImgPickDialogBinding
import com.pebbletechsolutions.memegenrator.ui.main.adapter.HomeRecyclerAdapter
import com.pebbletechsolutions.memegenrator.ui.main.adapter.SavedImageRecyclerAdapter
import com.pebbletechsolutions.memegenrator.ui.main.view.EditorActivity
import com.pebbletechsolutions.memegenrator.ui.main.view.ResultActivity
import com.pebbletechsolutions.memegenrator.utils.OnRecyclerItemClickListner
import com.pebbletechsolutions.memegenrator.utils.cropImgToResultActivity
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.io.IOException


class SavedImagesFrag : Fragment(), OnRecyclerItemClickListner {

    var passSav: Bundle = Bundle()

    private lateinit var cropImgResultLaunch: ActivityResultLauncher<Any?>

    private lateinit var savedRef: DatabaseReference
    private lateinit var savStoreRef: StorageReference
    private lateinit var savedImgList: ArrayList<FdbMemeModel>
    private lateinit var savedImgBottomSheet: ItemViewBottomSheetFrag


    private var savFragBind: FragmentSavedImagesBinding? = null
    private val SBind get() = savFragBind
    var isPhototaken = false
    var cropingPhotpPath: String = ""
    var cropImgName: String = "croppedImage"

    val cropFragResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return  CropImage.activity()
                .getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        savedImgList = arrayListOf<FdbMemeModel>()


        savedRef = FirebaseDatabase.getInstance().getReference("savedImgList")
        savStoreRef = FirebaseStorage.getInstance().reference
        savedImgBottomSheet = ItemViewBottomSheetFrag()

        cropImgResultLaunch = registerForActivityResult(cropFragResultContract){
            it?.let { uri ->
                val j = Intent(requireContext(), ResultActivity::class.java)
                j.putExtra("FrmSavImg", uri)
                startActivity(j)
            }
        }
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
            savFragBind!!.saveImgShimmer.visibility = View.GONE
        }else{
            savFragBind!!.notItemLyt.visibility = View.GONE
            savFragBind!!.saveImgShimmer.isVisible = true
            savFragBind!!.saveImgShimmer.startShimmer()
        }

        savFragBind!!.btnAddItem.setOnClickListener {
            cropImgResultLaunch.launch(null)
        }

        savFragBind!!.savedFragRV.layoutManager = LinearLayoutManager(requireContext())
        getSavedImgList()


        return view
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK){
            if (requestCode == 111){
                val editBundle: Bundle = Bundle()
                editBundle.putString("TakenPicutreFromSavedImg", cropingPhotpPath)
                parentFragmentManager.setFragmentResult("TakenSavedFromImg", editBundle)
                startActivity(Intent(requireContext(), EditorActivity::class.java))
//                val u: Intent = Intent(requireContext(), EditorActivity::class.java)
//                u.putExtra("takenSavedImg", isPhototaken)
//                u.putExtra("TakenPictureFromSavedImg", cropingPhotpPath)
//                startActivity(u)
            }else if(requestCode == 222){
                val pickUri: Uri = data?.data!!
                Log.e("Picked", pickUri.toString())
                val editBundleSlct: Bundle = Bundle()
                editBundleSlct.putString("SlctPicSavedImg", pickUri.toString())
                parentFragmentManager.setFragmentResult("PictureSelectFromSavedImg", editBundleSlct)
                startActivity(Intent(requireContext(), EditorActivity::class.java))
//                val k = Intent(requireContext(), EditorActivity::class.java)
//                k.putExtra("pickedSavedImg", isPhototaken)
//                k.putExtra("pickedImageFromSavedImg", pickUri)
//                startActivity(k)
            }

        }
    }

    fun showSavedImgBottomSheet(position: Int){
        passSav.putString("SavedFragImg", savedImgList[position].image)
        parentFragmentManager.setFragmentResult("FromSavedFrag", passSav)
        savedImgBottomSheet.show(requireActivity().supportFragmentManager, ItemViewBottomSheetFrag.TAG)

    }



    fun getSavedImgList(){
        savedRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (savedImgSnapshot in snapshot.children){
                        val meme = savedImgSnapshot.getValue(FdbMemeModel::class.java)
                        savedImgList.add(meme!!)
                    }
                    savFragBind!!.saveImgShimmer.stopShimmer()
                    savFragBind!!.saveImgShimmer.visibility = View.GONE
                    savFragBind!!.savedFragRV.adapter = SavedImageRecyclerAdapter(savedImgList, this@SavedImagesFrag)
            }else{

                savFragBind!!.notItemLyt.isVisible = true

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onItemClick(position: Int) {

    }

    override fun onItemLongClick(position: Int) {

    }

}



