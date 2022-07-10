package com.ubaya.protectcare04

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyAdapter(val activity:AppCompatActivity,val fragment:ArrayList<Fragment>): FragmentStateAdapter(activity){
    override fun getItemCount() =fragment.size

    override fun createFragment(position: Int) =fragment[position]
}