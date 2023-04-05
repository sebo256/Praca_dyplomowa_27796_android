package com.praca.dyplomowa.android.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.ActivityMainViewBinding
import com.praca.dyplomowa.android.utils.SessionManager

class MainActivityView : AppCompatActivity() {
    private lateinit var binding: ActivityMainViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(binding.fragmentContainerMainActivityView.id, JobsFragmentView()).commit()

        binding.navigationBar.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_item_1 -> {
                    supportFragmentManager.beginTransaction().replace(binding.fragmentContainerMainActivityView.id, JobsFragmentView()).commit()
                    true
                }
                R.id.menu_item_2 -> {
                    supportFragmentManager.beginTransaction().replace(binding.fragmentContainerMainActivityView.id, CalendarFragmentView()).commit()
                    true
                }
                R.id.menu_item_3 -> {
                    supportFragmentManager.beginTransaction().replace(binding.fragmentContainerMainActivityView.id, ProfileFragmentView()).commit()
                    true
                }
                else -> false
            }
        }
    }
}
