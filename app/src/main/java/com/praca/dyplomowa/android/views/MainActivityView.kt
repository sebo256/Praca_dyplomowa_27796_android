package com.praca.dyplomowa.android.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.ActivityMainViewBinding

class MainActivityView : AppCompatActivity() {
    private lateinit var binding: ActivityMainViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(binding.fragmentContainerMainActivityView.id, JobsFragmentView(),"JobsFragmentView").commit()


        binding.navigationBar.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_item_1 -> {
                    supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(binding.fragmentContainerMainActivityView.id, JobsFragmentView(),"JobsFragmentView").commit()
                    true
                }
                R.id.menu_item_2 -> {
                    supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(binding.fragmentContainerMainActivityView.id, CalendarFragmentView(), "CalendarFragmentView").commit()
                    true
                }
                R.id.menu_item_3 -> {
                    supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(binding.fragmentContainerMainActivityView.id, ProfileFragmentView(), "ProfileFragmentView").commit()
                    true
                }
                else -> false
            }
        }

    }



}
