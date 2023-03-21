package com.praca.dyplomowa.android.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.praca.dyplomowa.android.databinding.ActivityJobApplyToViewBinding
import com.praca.dyplomowa.android.viewmodels.JobApplyToViewModel
import com.praca.dyplomowa.android.views.adapters.JobApplyToAdapter
import kotlinx.coroutines.processNextEventInCurrentThread
import okhttp3.internal.notifyAll
import okhttp3.internal.wait

private lateinit var binding: ActivityJobApplyToViewBinding
private lateinit var jobApplyToAdapter: JobApplyToAdapter
lateinit var viewModelJobApplyToViewModel: JobApplyToViewModel


class JobApplyToActivityView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobApplyToViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelJobApplyToViewModel = ViewModelProvider(this).get(JobApplyToViewModel::class.java)
        binding.recyclewViewJobApplyTo.layoutManager = LinearLayoutManager(this)
        getUsers()

        binding.buttonSaveJobApplyTo.setOnClickListener {
            applyJobTo()
        }
    }

    fun getUsers(){
        viewModelJobApplyToViewModel.userResult.observe(this){
            print(it)
            jobApplyToAdapter = JobApplyToAdapter(it)
            binding.recyclewViewJobApplyTo.adapter = jobApplyToAdapter
        }
        viewModelJobApplyToViewModel.getUsers()
    }

    fun applyJobTo(){
        viewModelJobApplyToViewModel.jobResult.observe(this){
            finish()
        }
        viewModelJobApplyToViewModel.addJobApplyTo(
            objectId = intent.getStringExtra("newJobId")!!,
            jobAppliedTo = jobApplyToAdapter.getCheckedUsers()
        )
    }


}