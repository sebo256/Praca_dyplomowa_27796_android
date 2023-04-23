package com.praca.dyplomowa.android.views

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentJobsViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.FragmentNavigationUtils
import com.praca.dyplomowa.android.utils.RecyclerViewJobsUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.JobsViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter

class JobsFragmentView : Fragment(R.layout.fragment_jobs_view) {

    private var _binding: FragmentJobsViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelJobs: JobsViewModel
    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentJobsViewBinding.inflate(inflater, container, false)



        binding.recyclerViewJob.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(this.recyclerViewJobsUtilsInterface)
        binding.recyclerViewJob.adapter = jobAdapter

        viewModelJobs = ViewModelProvider(requireActivity()).get(JobsViewModel::class.java)
        setObserverForGetJobRequestJobs()
        setObserverForDeleteJob()
        setObserverForError()
        setObserverForScrollingToNewItem()
        getJobs()


        binding.buttonAddJobJobFragment.setOnClickListener{
            FragmentNavigationUtils.addFragmentFadeWithSourceFragment(
                fragmentManager = parentFragmentManager,
                fragment = JobAddFragmentView(),
                argumentSourceFragmentName = "JobsFragmentView"
            )
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
                TransitionManager.beginDelayedTransition(binding.root)
                if(parentFragmentManager.backStackEntryCount == 0){
                    when(binding.textFieldLayoutSearchJobJobFragment.visibility == View.VISIBLE){
                        true -> hideSearchBarAndShowFullList()
                        false -> requireActivity().finish()
                    }
                }else{
                    parentFragmentManager.popBackStack()
                }
            }
        })

        return binding.root
    }

    fun getJobs(){
        viewModelJobs.getJobs()
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

    private fun setObserverForScrollingToNewItem() {
        jobAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionToScroll: Int, count: Int) {
                binding.recyclerViewJob.smoothScrollToPosition(positionToScroll)
            }
        })
    }

    private fun hideSearchBarAndShowFullList(){
        jobAdapter.filter.filter("")
        binding.textFieldTextSearchJobJobFragment.setText("")
        binding.textFieldLayoutSearchJobJobFragment.visibility = View.GONE
    }

    private val recyclerViewJobsUtilsInterface: RecyclerViewJobsUtilsInterface = object : RecyclerViewJobsUtilsInterface {
        override fun onClick(string: String) {
            FragmentNavigationUtils.addFragmentFadeWithOneStringBundleValueAndSourceFragment(
                fragmentManager = parentFragmentManager,
                fragment = JobAddFragmentView(),
                argumentKey = "jobObjectId",
                argumentValue = string,
                argumentSourceFragmentName = "JobsFragmentView"
            )
        }

        override fun onLongClick(string: String) {
            if(SessionManager.getIsAdmin(requireContext())){
                val materialDialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.dialog_joblist_text_title)
                    .setMessage(R.string.dialog_joblist_text_message)
                    .setPositiveButton(R.string.dialog_joblist_positive_title) { dialog, which ->
                        FragmentNavigationUtils.addFragmentOpenWithOneStringBundleValueAndSourceFragment(
                            fragmentManager = parentFragmentManager,
                            fragment = JobApplyToFragmentView(),
                            argumentKey = "jobId",
                            argumentValue = string,
                            argumentSourceFragmentName = "JobsFragmentView"
                        )
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
    }

}