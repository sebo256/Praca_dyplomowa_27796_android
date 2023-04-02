package com.praca.dyplomowa.android.views

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import com.praca.dyplomowa.android.api.response.JobGetDatesAndInfoResponse
import com.praca.dyplomowa.android.databinding.FragmentCalendarJobListBinding
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.views.adapters.JobAdapter

class CalendarJobListFragment : DialogFragment() {
    private var _binding: FragmentCalendarJobListBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobAdapter: JobAdapter
    var jobList: MutableList<JobGetAllResponse> = mutableListOf()

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

        val getArguments = arguments?.getString("jobsList")
        val jobGetAllResponseCollection = Gson().fromJson(getArguments, JobGetAllResponseCollection::class.java)


        binding.recyclerViewCalendarJobListFragment.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(recyclerViewUtilsInterface)
        binding.recyclerViewCalendarJobListFragment.adapter = jobAdapter
        jobAdapter.setupData(jobGetAllResponseCollection.collection.toMutableList())


        return view
    }

    private fun setupBackground(binding: FragmentCalendarJobListBinding) {
        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
            true -> binding.layoutCalendarJobListFragment.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.background_dark))
            false -> binding.layoutCalendarJobListFragment.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))

        }
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
                    viewModelJobs.deleteJob(string)
//                    getJobsAndUpdateRecyclerData()
                }

            if(!SessionManager.getIsAdmin(requireContext())) {
                materialDialog.setNegativeButton("") { dialog, which -> }
            }
            materialDialog.show()
        }
    }



    companion object {

        fun newInstance(jobs: JobGetAllResponseCollection): CalendarJobListFragment = CalendarJobListFragment().apply {
            arguments = Bundle().apply {
                putString("jobsList", Gson().toJson(jobs))
            }
        }
    }

}

