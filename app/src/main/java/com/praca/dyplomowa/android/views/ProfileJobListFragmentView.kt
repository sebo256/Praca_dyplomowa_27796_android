package com.praca.dyplomowa.android.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentProfileJobListViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.FragmentNavigationUtils
import com.praca.dyplomowa.android.utils.RecyclerViewUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.ProfileJobListViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter

class ProfileJobListFragmentView : Fragment() {

    private var _binding: FragmentProfileJobListViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelProfileJobList: ProfileJobListViewModel
    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileJobListViewBinding.inflate(inflater, container, false)

        viewModelProfileJobList = ViewModelProvider(this).get(ProfileJobListViewModel::class.java)
        setObserverForError()
        setObserverForGetCompletedJobsAppliedToUser()
        setObserverForGetTodoJobsAppliedToUser()
        setObserverForDeleteJob()

        binding.textViewTitleJobsListFragmentProfileView.setText(arguments?.getString("title"))
        binding.recyclerViewJobsListFragmentProfileView.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(recyclerViewUtilsInterface)
        binding.recyclerViewJobsListFragmentProfileView.adapter = jobAdapter
        getJobsAndUpdateRecyclerData()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(parentFragmentManager.backStackEntryCount == 1){
                    val fragment = parentFragmentManager.findFragmentByTag("ProfileFragmentView") as ProfileFragmentView
                    fragment.setupProfileDataWithoutName()
                    parentFragmentManager.popBackStack()
                }else if(parentFragmentManager.backStackEntryCount > 1){
                    parentFragmentManager.popBackStack()
                }
            }
        })

        return binding.root
    }

    private fun setObserverForGetCompletedJobsAppliedToUser(){
        viewModelProfileJobList.jobResult.observe(viewLifecycleOwner){
            jobAdapter.setupData(it.collection.toList())
        }
    }

    private fun setObserverForGetTodoJobsAppliedToUser(){
        viewModelProfileJobList.jobResult.observe(viewLifecycleOwner){
            jobAdapter.setupData(it.collection.toList())
        }
    }

    private fun setObserverForDeleteJob(){
        viewModelProfileJobList.jobDeleteResult.observe(viewLifecycleOwner){
            getJobsAndUpdateRecyclerData()
        }
    }

    private fun setObserverForError() {
        viewModelProfileJobList.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelProfileJobList.errorResult.value = false
            }
        }
    }

    fun getJobsAndUpdateRecyclerData(){
        when(arguments?.getString("title") == resources.getString(R.string.textview_list_jobs_completed)){
            true -> viewModelProfileJobList.getCompletedJobsAppliedToUser(SessionManager.getCurrentUserUsername(requireContext())!!)
            false -> viewModelProfileJobList.getTodoJobsAppliedToUser(SessionManager.getCurrentUserUsername(requireContext())!!)
        }
    }

    private val recyclerViewUtilsInterface: RecyclerViewUtilsInterface = object :
        RecyclerViewUtilsInterface {
        override fun onClick(string: String) {
            FragmentNavigationUtils.addFragmentFadeWithOneStringBundleValueAndSourceFragment(
                fragmentManager = parentFragmentManager,
                fragment = JobAddFragmentView(),
                argumentKey = "jobObjectId",
                argumentValue = string,
                argumentSourceFragmentName = "ProfileJobListFragmentView"
            )
        }

        override fun onLongClick(string: String) {
            val materialDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_joblist_text_title)
                .setMessage(R.string.dialog_joblist_text_message)
                .setPositiveButton(R.string.dialog_joblist_positive_title) { dialog, which ->
                    FragmentNavigationUtils.addFragmentOpenWithOneStringBundleValueAndSourceFragment(
                        fragmentManager = parentFragmentManager,
                        fragment = JobApplyToFragmentView(),
                        argumentKey = "jobId",
                        argumentValue = string,
                        argumentSourceFragmentName = "ProfileJobListFragmentView"
                    )
                }
                .setNeutralButton(R.string.dialog_joblist_neutral_title) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_joblist_negative_title) { dialog, which ->
                    viewModelProfileJobList.deleteJob(string)
                }

            if(!SessionManager.getIsAdmin(requireContext())) {
                materialDialog.setNegativeButton("") { dialog, which -> }
            }
            materialDialog.show()
        }
    }

}