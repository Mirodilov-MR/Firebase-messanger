package com.example.firebase_messanger.adapters.pageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*


class SignInUpAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var fragmentArrayList = ArrayList<Fragment>()
    var stringArrayList = ArrayList<String>()
    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getCount(): Int {
        return stringArrayList.size
    }

    fun addPagerFragment(fragment: Fragment, s: String) {
        fragmentArrayList.add(fragment)
        stringArrayList.add(s)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return stringArrayList[position]
    }
}