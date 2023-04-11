package com.praca.dyplomowa.android.views

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.ActivityCalendarJobListViewBinding
import com.praca.dyplomowa.android.utils.DateRange
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.CalendarJobListViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter


class CalendarJobListView : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarJobListViewBinding
    private lateinit var viewModelCalendarJobList: CalendarJobListViewModel
    private lateinit var jobAdapter: JobAdapter
    private lateinit var dateRange: DateRange

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarJobListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBackground()

        dateRange = Gson().fromJson(intent.getStringExtra("dateRange")!!, DateRange::class.java)
        viewModelCalendarJobList = ViewModelProvider(this).get(CalendarJobListViewModel::class.java)
        setObserverForJobByLongDateBetween()
        setObserverForDeleteJob()
        setObserverForError()

        binding.textViewDateCalendarJobListActivityView.setText(viewModelCalendarJobList.calculateDate(dateRange.startLong+1))
        binding.recyclerViewCalendarJobListActivity.layoutManager = LinearLayoutManager(this)
        jobAdapter = JobAdapter(recyclerViewUtilsInterface)
        binding.recyclerViewCalendarJobListActivity.adapter = jobAdapter

    }

    private fun setupBackground() {
        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
            true -> binding.layoutCalendarJobListActivity.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                R.color.background_dark
            ))
            false -> binding.layoutCalendarJobListActivity.setBackgroundColor(ContextCompat.getColor(binding.root.context,
                R.color.white
            ))

        }
    }

    private fun setObserverForJobByLongDateBetween(){
        viewModelCalendarJobList.jobResult.observe(this){
            jobAdapter.setupData(it.collection.toList())
        }
    }

    private fun setObserverForDeleteJob(){
        viewModelCalendarJobList.jobDeleteResult.observe(this){
            getJobsAndUpdateRecyclerData()
        }
    }

    private fun setObserverForError() {
        viewModelCalendarJobList.errorResult.observe(this){
            if(it == true) {
                ErrorDialogHandler(this)
                viewModelCalendarJobList.errorResult.value = false
            }
        }
    }


    private fun getJobsAndUpdateRecyclerData(){
        viewModelCalendarJobList.getJobByLongDateBetween(startLong = dateRange.startLong, endLong = dateRange.endLong)
    }

    private val recyclerViewUtilsInterface: RecyclerViewUtilsInterface = object : RecyclerViewUtilsInterface {
        override fun onClick(string: String) {
            val intent = Intent(this@CalendarJobListView, AddJobActivity::class.java)
            intent.putExtra("jobObjectId",string)
            startActivity(intent)
        }

        override fun onLongClick(string: String) {
            val materialDialog = MaterialAlertDialogBuilder(this@CalendarJobListView)
                .setTitle(R.string.dialog_joblist_text_title)
                .setMessage(R.string.dialog_joblist_text_message)
                .setPositiveButton(R.string.dialog_joblist_positive_title) { dialog, which ->
                    val intent = Intent(this@CalendarJobListView, JobApplyToActivityView::class.java)
                    intent.putExtra("jobId", string)
                    startActivity(intent)
                }
                .setNeutralButton(R.string.dialog_joblist_neutral_title) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_joblist_negative_title) { dialog, which ->
                    viewModelCalendarJobList.deleteJob(string)
                }

            if(!SessionManager.getIsAdmin(this@CalendarJobListView)) {
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