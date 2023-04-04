package com.praca.dyplomowa.android.views

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import com.praca.dyplomowa.android.api.response.JobGetDatesAndInfoResponse
import com.praca.dyplomowa.android.databinding.FragmentCalendarJobListBinding
import com.praca.dyplomowa.android.utils.DateRange
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.CalendarJobListViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter

lateinit var viewModelCalendarJobList: CalendarJobListViewModel

class CalendarJobListFragment : Fragment() {
    private var _binding: FragmentCalendarJobListBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobAdapter: JobAdapter
    private var jobList: List<JobGetAllResponse> = listOf()
    private lateinit var dateRange: DateRange
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarJobListBinding.inflate(inflater, container, false)
        val view = binding.root

        setupBackground(binding)
        binding.recyclerViewCalendarJobListFragment.visibility = View.INVISIBLE

        viewModelCalendarJobList = ViewModelProvider(requireActivity()).get(CalendarJobListViewModel::class.java)
        setObserverForJobByLongDateBetween()
        setObserverForDeleteJob()

        val getArguments = arguments?.getString("dateRange")!!
        dateRange = Gson().fromJson(getArguments, DateRange::class.java)


        binding.recyclerViewCalendarJobListFragment.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(recyclerViewUtilsInterface)
        binding.recyclerViewCalendarJobListFragment.adapter = jobAdapter

        return view
    }

    private fun setupBackground(binding: FragmentCalendarJobListBinding) {
        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
            true -> binding.layoutCalendarJobListFragment.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.background_dark))
            false -> binding.layoutCalendarJobListFragment.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))

        }
    }

    private fun setObserverForJobByLongDateBetween(){
        viewModelCalendarJobList.jobResult.observe(viewLifecycleOwner){
            jobAdapter.setupData(it.collection.toList())
            binding.recyclerViewCalendarJobListFragment.visibility = View.VISIBLE
        }
    }

    private fun setObserverForDeleteJob(){
        viewModelCalendarJobList.jobDeleteResult.observe(viewLifecycleOwner){
            getJobsAndUpdateRecyclerData()
        }
    }

    private fun getJobsAndUpdateRecyclerData(){
        viewModelCalendarJobList.getJobByLongDateBetween(startLong = dateRange.startLong, endLong = dateRange.endLong)
    }

    private val recyclerViewUtilsInterface: RecyclerViewUtilsInterface = object : RecyclerViewUtilsInterface {
        override fun onClick(string: String) {
            val intent = Intent(requireContext(), AddJobActivity::class.java)
            intent.putExtra("jobObjectId",string)
            startActivity(intent)
        }

        override fun onLongClick(string: String) {
            val materialDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_joblist_text_title)
                .setMessage(R.string.dialog_joblist_text_message)
                .setPositiveButton(R.string.dialog_joblist_positive_title) { dialog, which ->
                    val intent = Intent(requireContext(), JobApplyToActivityView::class.java)
                    intent.putExtra("jobId", string)
                    startActivity(intent)
                }
                .setNeutralButton(R.string.dialog_joblist_neutral_title) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_joblist_negative_title) { dialog, which ->
                    viewModelCalendarJobList.deleteJob(string)
                }

            if(!SessionManager.getIsAdmin(requireContext())) {
                materialDialog.setNegativeButton("") { dialog, which -> }
            }
            materialDialog.show()
        }
    }
    override fun onResume() {
        super.onResume()
        getJobsAndUpdateRecyclerData()

    }



    companion object {

        fun newInstance(dateRange: DateRange): CalendarJobListFragment = CalendarJobListFragment().apply {
            arguments = Bundle().apply {
                putString("dateRange", Gson().toJson(dateRange))
            }
        }
    }

}

