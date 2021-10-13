package com.pebbletechsolutions.memegenrator.ui.main.view

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.pebbletechsolutions.memegenrator.R
import com.pebbletechsolutions.memegenrator.databinding.ActivityMainBinding
import com.pebbletechsolutions.memegenrator.ui.main.adapter.HomeRecyclerAdapter
import com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes.FavouriteFrag
import com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes.HomeFragment
import com.pebbletechsolutions.memegenrator.ui.main.view.Fragemes.SavedImagesFrag
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_recycler_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import androidx.core.widget.NestedScrollView
import com.google.android.material.animation.AnimationUtils


class HomeActivity : AppCompatActivity() {

    private lateinit var HABind: ActivityMainBinding
    var isFabOpen = false


    private val fabClose: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_close) }
    private val fabOpen: Animation by lazy { android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_open) }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        HABind = ActivityMainBinding.inflate(layoutInflater)
        val view = HABind.root
        setContentView(view)
        replaceFragment(HomeFragment())

        HABind.homeBtnNav.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.btmNavHome -> {
                    replaceFragment(HomeFragment())
                }
                R.id.btnNavSI -> {
                    replaceFragment(SavedImagesFrag())
                }
                R.id.btnNavFav -> {
                    replaceFragment(FavouriteFrag())
                }
            }
            return@setOnItemSelectedListener true
        }

        HABind.homeCreateFab.setOnClickListener {
            animateFab()
        }

        HABind.homeNstdSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY -> // the delay of the extension of the FAB is set for 12 items
            if (scrollY > oldScrollY + 12 && HABind.homeCreateFab.isExtended) {
                HABind.homeCreateFab.shrink()
            }

            // the delay of the extension of the FAB is set for 12 items
            if (scrollY < oldScrollY - 12 && !HABind.homeCreateFab.isExtended) {
                HABind.homeCreateFab.extend()
            }

            // if the nestedScrollView is at the first item of the list then the
            // extended floating action should be in extended state
            if (scrollY == 0) {
                HABind.homeCreateFab.extend()
            }
        })


    }

    private fun animateFab()
    {
        if (isFabOpen){
            HABind.homeSlctFileFab.startAnimation(fabClose)
            HABind.homeSlctFileFab.visibility = View.GONE
            HABind.homeTakePhotoFab.startAnimation(fabClose)
            HABind.homeTakePhotoFab.visibility = View.GONE
            isFabOpen = false
        }else{
            HABind.homeSlctFileFab.startAnimation(fabOpen)
            HABind.homeSlctFileFab.visibility = View.VISIBLE
            HABind.homeTakePhotoFab.startAnimation(fabOpen)
            HABind.homeTakePhotoFab.visibility = View.VISIBLE
            isFabOpen = true
        }

    }

    private fun replaceFragment(frag: Fragment) {
        val fragTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.replace(R.id.FragContainer, frag)
        fragTransaction.commit()

    }


}