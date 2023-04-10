package com.praca.dyplomowa.android.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobTimeSpentResponse
import com.praca.dyplomowa.android.databinding.ActivityProfileTimeSpentListViewBinding
import com.praca.dyplomowa.android.utils.DatesForTimeSpent
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.ProfileTimeSpentListViewModel
import com.praca.dyplomowa.android.views.adapters.ProfileTimeSpentAdapter
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.util.*


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
}