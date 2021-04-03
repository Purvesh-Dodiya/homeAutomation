package com.homeautomation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.homeautomation.adapter.ViewPagerAdapter
import com.homeautomation.fragment.HallFrament
import com.homeautomation.fragment.KitchenFragment
import com.homeautomation.fragment.RoomFragment
import kotlinx.android.synthetic.main.activity_main.bottomNav
import kotlinx.android.synthetic.main.activity_main.viewPagerContainer

class MainActivity : AppCompatActivity() {
    val titleArray : ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleArray.apply {
            add("Hall")
            add("Room")
            add("Kitchen")
        }
        intialSetUp()


    }

    private fun intialSetUp() {
        // Initialize Firebase Auth
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser == null){
            Intent(applicationContext,LoginActivity::class.java).apply {
                startActivity(this)
            }
        } else {

            ViewPagerAdapter(supportFragmentManager,lifecycle).apply {
                add(HallFrament())
                add(RoomFragment())
                add(KitchenFragment())
                viewPagerContainer.adapter = this
            }

            bottomNav.setOnNavigationItemSelectedListener {
                val index = when (it.itemId) {
                    R.id.hall -> 0
                    R.id.room -> 1
                    R.id.kitchen -> 2
                    else -> 0
                }
                //supportFragmentManager.beginTransaction().replace(R.id.viewPagerContainer, HallFrament()).addToBackStack(null).commit()
             viewPagerContainer.setCurrentItem(index, true)
                true
            }

        }
    }
}
