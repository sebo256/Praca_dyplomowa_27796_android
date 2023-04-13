package com.praca.dyplomowa.android.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.praca.dyplomowa.android.api.response.JobTimeSpentResponse
import com.praca.dyplomowa.android.databinding.ActivityProfileTimeSpentListViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.ProfileTimeSpentListViewModel
import com.praca.dyplomowa.android.views.adapters.ProfileTimeSpentAdapter


class ProfileTimeSpentListView : AppCompatActivity() {

    private lateinit var binding: ActivityProfileTimeSpentListViewBinding
    private lateinit var viewModelProfileTimeSpentList: ProfileTimeSpentListViewModel
    private lateinit var timeSpentAdapter: ProfileTimeSpentAdapter
    private var list: MutableList<JobTimeSpentResponse> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileTimeSpentListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelProfileTimeSpentList = ViewModelProvider(this).get(ProfileTimeSpentListViewModel::class.java)
        setObserverForError()
        setObserverForGetAllTimeSpentForUserPerMonth()

        binding.recyclerTimeSpent.layoutManager = LinearLayoutManager(this)
        timeSpentAdapter = ProfileTimeSpentAdapter()
        binding.recyclerTimeSpent.adapter = timeSpentAdapter

    }

    private fun setObserverForGetAllTimeSpentForUserPerMonth(){
        viewModelProfileTimeSpentList.jobTimeSpentResult.observe(this){
            timeSpentAdapter.setupData(it.collection.toList())
        }
        when(intent.getStringExtra("username") == null){
            true -> viewModelProfileTimeSpentList.getAllTimeSpentForUserPerMonth(SessionManager.getCurrentUserUsername(this)!!)
            false -> viewModelProfileTimeSpentList.getAllTimeSpentForUserPerMonth(intent.getStringExtra("username")!!)
        }
    }

    private fun setObserverForError() {
        viewModelProfileTimeSpentList.errorResult.observe(this){
            if(it == true) {
                ErrorDialogHandler(this)
                viewModelProfileTimeSpentList.errorResult.value = false
            }
        }
    }
}