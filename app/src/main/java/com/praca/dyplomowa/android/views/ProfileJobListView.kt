package com.praca.dyplomowa.android.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.ActivityProfileJobListViewBinding
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.ProfileJobListViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter

lateinit var viewModelProfileJobList: ProfileJobListViewModel

class ProfileJobListView : AppCompatActivity() {

    private lateinit var binding: ActivityProfileJobListViewBinding
    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileJobListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelProfileJobList = ViewModelProvider(this).get(ProfileJobListViewModel::class.java)
        setObserverForGetCompletedJobsAppliedToUser()
        setObserverForGetTodoJobsAppliedToUser()
        setObserverForDeleteJob()

        binding.textViewTitleJobsListProfileView.setText(intent.getStringExtra("title"))
        binding.recyclerViewJobsListProfileView.layoutManager = LinearLayoutManager(this)
        jobAdapter = JobAdapter(recyclerViewUtilsInterface)
        binding.recyclerViewJobsListProfileView.adapter = jobAdapter

    }

    private fun setObserverForGetCompletedJobsAppliedToUser(){
        viewModelProfileJobList.jobResult.observe(this){
            jobAdapter.setupData(it.collection.toList())
        }
    }

    private fun setObserverForGetTodoJobsAppliedToUser(){
        viewModelProfileJobList.jobResult.observe(this){
            jobAdapter.setupData(it.collection.toList())
        }
    }

    private fun setObserverForDeleteJob(){
        viewModelProfileJobList.jobDeleteResult.observe(this){
            getJobsAndUpdateRecyclerData()
        }
    }

    private fun getJobsAndUpdateRecyclerData(){
        when(intent.getStringExtra("title") == resources.getString(R.string.textview_list_jobs_completed)){
            true -> viewModelProfileJobList.getCompletedJobsAppliedToUser(SessionManager.getCurrentUserUsername(this)!!)
            false -> viewModelProfileJobList.getTodoJobsAppliedToUser(SessionManager.getCurrentUserUsername(this)!!)
        }
    }

    private val recyclerViewUtilsInterface: RecyclerViewUtilsInterface = object :
        RecyclerViewUtilsInterface {
        override fun onClick(string: String) {
            val intent = Intent(this@ProfileJobListView, AddJobActivity::class.java)
            intent.putExtra("jobObjectId",string)
            startActivity(intent)
        }

        override fun onLongClick(string: String) {
            val materialDialog = MaterialAlertDialogBuilder(this@ProfileJobListView)
                .setTitle(R.string.dialog_joblist_text_title)
                .setMessage(R.string.dialog_joblist_text_message)
                .setPositiveButton(R.string.dialog_joblist_positive_title) { dialog, which ->
                    val intent = Intent(this@ProfileJobListView, JobApplyToActivityView::class.java)
                    intent.putExtra("jobId", string)
                    startActivity(intent)
                }
                .setNeutralButton(R.string.dialog_joblist_neutral_title) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_joblist_negative_title) { dialog, which ->
                    viewModelCalendarJobList.deleteJob(string)
                }

            if(!SessionManager.getIsAdmin(this@ProfileJobListView)) {
                materialDialog.setNegativeButton("") { dialog, which -> }
            }
            materialDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        getJobsAndUpdateRecyclerData()
    }

}