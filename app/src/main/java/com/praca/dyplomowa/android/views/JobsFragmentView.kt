package com.praca.dyplomowa.android.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.databinding.FragmentJobsViewBinding
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.JobsViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter
private lateinit var viewModelJobs: JobsViewModel
class JobsFragmentView : Fragment(R.layout.fragment_jobs_view) {

    private var _binding: FragmentJobsViewBinding? = null
    private val binding get() = _binding!!
    var jobList: MutableList<JobGetAllResponse>? = mutableListOf()
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

        viewModelJobs = ViewModelProvider(requireActivity()).get(JobsViewModel::class.java)
        setObserverForGetJobRequestJobs()
        setObserverForDeleteJob()


        binding.recyclerViewJob.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(recyclerViewUtilsInterface)
        binding.recyclerViewJob.adapter = jobAdapter



        binding.buttonAddJobJobFragment.setOnClickListener{
            val intent = Intent(requireContext(), AddJobActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    fun setObserverForGetJobRequestJobs(){
        viewModelJobs.jobResult.observe(viewLifecycleOwner){
            println(it.collection)
            jobAdapter.setupData(it.collection.toList())
        }

    }

    fun setObserverForDeleteJob(){
        viewModelJobs.jobDeleteResult.observe(viewLifecycleOwner){
            println(it)
            viewModelJobs.getJobs()

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
                }

            if(!SessionManager.getIsAdmin(requireContext())) {
                materialDialog.setNegativeButton("") { dialog, which -> }
            }
            materialDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelJobs.getJobs()
    }

}