package com.praca.dyplomowa.android.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.praca.dyplomowa.android.databinding.ActivityJobApplyToViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.viewmodels.JobApplyToViewModel
import com.praca.dyplomowa.android.views.adapters.JobApplyToAdapter


class JobApplyToActivityView : AppCompatActivity() {
    private lateinit var binding: ActivityJobApplyToViewBinding
    private lateinit var jobApplyToAdapter: JobApplyToAdapter
    lateinit var viewModelJobApplyToViewModel: JobApplyToViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobApplyToViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelJobApplyToViewModel = ViewModelProvider(this).get(JobApplyToViewModel::class.java)
        binding.recyclewViewJobApplyTo.layoutManager = LinearLayoutManager(this)
        setObserverForError()
        setObserverForGetJobAppliedTo()

        binding.buttonSaveJobApplyTo.setOnClickListener {
            setObserverForApplyJobTo()
        }
    }

    private fun getUsers(appliedUsers: Collection<String>){
        viewModelJobApplyToViewModel.userResult.observe(this){
            print(it)
            jobApplyToAdapter = JobApplyToAdapter(appliedUsers = appliedUsers)
            binding.recyclewViewJobApplyTo.adapter = jobApplyToAdapter
            jobApplyToAdapter.setupData(it.collection.toList())
        }
        viewModelJobApplyToViewModel.getUsers()
    }

    private fun setObserverForGetJobAppliedTo(){
        viewModelJobApplyToViewModel.jobAppliedToRequestResult.observe(this){
            print(it)
            getUsers(it.jobAppliedTo)
        }
        viewModelJobApplyToViewModel.getJobAppliedTo(intent.getStringExtra("jobId")!!)
    }

    private fun setObserverForApplyJobTo(){
        viewModelJobApplyToViewModel.jobResult.observe(this){
            finish()
        }
        viewModelJobApplyToViewModel.addJobApplyTo(
            objectId = intent.getStringExtra("jobId")!!,
            jobAppliedTo = jobApplyToAdapter.getCheckedUsers()
        )
    }

    private fun setObserverForError() {
        viewModelJobApplyToViewModel.errorResult.observe(this){
            if(it == true) {
                ErrorDialogHandler(this)
                viewModelJobApplyToViewModel.errorResult.value = false
            }
        }
    }



}