package com.homeautomation

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.homeautomation.adapter.ViewPagerAdapter
import com.homeautomation.fragment.HallFrament
import com.homeautomation.fragment.KitchenFragment
import com.homeautomation.fragment.RoomFragment
import kotlinx.android.synthetic.main.activity_main.bottomNav
import kotlinx.android.synthetic.main.activity_main.viewPagerContainer

class MainActivity : AppCompatActivity() {
    private val titleArray : ArrayList<String> = arrayListOf()
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
            Intent(applicationContext, LoginActivity::class.java).apply {
                startActivity(this)
            }
        } else {

            ViewPagerAdapter(supportFragmentManager, lifecycle).apply {
                add(HallFrament())
                add(RoomFragment())
                add(KitchenFragment())
                viewPagerContainer.adapter = this
                viewPagerContainer.offscreenPageLimit = 3
            }
            viewPagerContainer.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val index = when (position) {
                        0 -> R.id.hall
                        1-> R.id.room
                        2 -> R.id.kitchen
                        else -> R.id.hall
                    }
                    bottomNav.selectedItemId = index
                }

            })

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        when (item.itemId) {
            R.id.btnLogout -> {
              Firebase.auth.signOut()
                Intent(applicationContext,LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(this)
                }
            }

            R.id.btnAdd -> {
                Intent(applicationContext,AddActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
