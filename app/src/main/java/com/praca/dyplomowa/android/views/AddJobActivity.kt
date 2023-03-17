package com.praca.dyplomowa.android.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.praca.dyplomowa.android.databinding.ActivityAddJobBinding

private lateinit var binding: ActivityAddJobBinding

class AddJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}