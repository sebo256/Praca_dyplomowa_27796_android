package com.praca.dyplomowa.android.views

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ExpandableListView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.search.SearchBar
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.databinding.FragmentJobsViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.JobsViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter

class JobsFragmentView : Fragment(R.layout.fragment_jobs_view) {

    private var _binding: FragmentJobsViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelJobs: JobsViewModel
    private lateinit var jobAdapter: JobAdapter

    var jobList: MutableList<JobGetAllResponse>? = mutableListOf()


    override fun onStart() {
        super.onStart()

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
        setObserverForError()



        binding.recyclerViewJob.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(recyclerViewUtilsInterface)
        binding.recyclerViewJob.adapter = jobAdapter



        binding.buttonAddJobJobFragment.setOnClickListener{
            val intent = Intent(requireContext(), AddJobActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSearchJobJobFragment.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root)
            when(binding.textFieldLayoutSearchJobJobFragment.visibility == View.GONE){
                true -> binding.textFieldLayoutSearchJobJobFragment.visibility = View.VISIBLE
                false -> hideSearchBarAndShowFullList()
            }
        }

        binding.textFieldTextSearchJobJobFragment.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                jobAdapter.filter.filter(binding.textFieldTextSearchJobJobFragment.text)
            }
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                hideSearchBarAndShowFullList()
            }
        })

        return view
    }

    fun setObserverForGetJobRequestJobs(){
        viewModelJobs.jobResult.observe(viewLifecycleOwner){
            jobAdapter.setupData(it.collection.toList())
        }

    }

    fun setObserverForDeleteJob(){
        viewModelJobs.jobDeleteResult.observe(viewLifecycleOwner){
            viewModelJobs.getJobs()
        }
    }

    private fun setObserverForError() {
        viewModelJobs.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelJobs.errorResult.value = false
            }
        }
    }

    private fun hideSearchBarAndShowFullList(){
        jobAdapter.filter.filter("")
        binding.textFieldTextSearchJobJobFragment.setText("")
        binding.textFieldLayoutSearchJobJobFragment.visibility = View.GONE
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