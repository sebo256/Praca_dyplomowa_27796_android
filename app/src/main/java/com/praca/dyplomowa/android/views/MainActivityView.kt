package com.praca.dyplomowa.android.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.praca.dyplomowa.android.databinding.ActivityMainViewBinding
import com.praca.dyplomowa.android.utils.SessionManager

private lateinit var binding: ActivityMainViewBinding

class MainActivityView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.buttonLogoutMainActivity.setOnClickListener {
            SessionManager.clearSharedPrefs(applicationContext)
            finish()
        }
    }
}