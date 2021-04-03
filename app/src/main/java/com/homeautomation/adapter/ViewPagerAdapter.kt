package com.homeautomation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager : FragmentManager, lifecycle : Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragment : ArrayList<Fragment> = ArrayList()
    fun add(fragmentName : Fragment) {
        fragment.add(fragmentName)
    }

    override fun getItemCount() : Int = fragment.count()

    override fun createFragment(position : Int) : Fragment = fragment[position]
}
