package com.praca.dyplomowa.android.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.databinding.FragmentJobsViewBinding
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.viewmodels.JobsViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter

lateinit var viewModelJobs: JobsViewModel

class JobsFragmentView : Fragment(R.layout.fragment_jobs_view) {
    private var _binding: FragmentJobsViewBinding? = null
    private val binding get() = _binding!!
    var jobList: MutableList<JobGetAllResponse> = mutableListOf()
    private lateinit var jobAdapter: JobAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentJobsViewBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerViewJob.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(recyclerViewUtilsInterface)
        binding.recyclerViewJob.adapter = jobAdapter

        binding.buttonAddJobJobFragment.setOnClickListener{
            val intent = Intent(requireContext(), AddJobActivity::class.java)
            startActivity(intent)
        }

        setObserverForJobRequestJobs()
        return view
    }

    fun setObserverForJobRequestJobs(){

        viewModelJobs = ViewModelProvider(requireActivity()).get(JobsViewModel::class.java)
        viewModelJobs.jobResult.observe(requireActivity()){
            println(it.collection)
            jobList = (it.collection.toMutableList())
            jobAdapter.setupData(it.collection.toMutableList())
        }

    }

    fun getJobsAndUpdateRecyclerData(){
        viewModelJobs.getJobs()
        jobAdapter.setupData(jobList)
    }

    private val recyclerViewUtilsInterface: RecyclerViewUtilsInterface = object : RecyclerViewUtilsInterface {
        override fun onClick(string: String) {
            val intent = Intent(requireContext(), AddJobActivity::class.java)
            intent.putExtra("jobObjectId",string)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getJobsAndUpdateRecyclerData()
    }


}