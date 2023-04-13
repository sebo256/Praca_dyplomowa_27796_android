package com.praca.dyplomowa.android.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentJobApplyToViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.viewmodels.JobApplyToViewModel
import com.praca.dyplomowa.android.views.adapters.JobApplyToAdapter

class JobApplyToFragmentView : Fragment() {
    private var _binding: FragmentJobApplyToViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobApplyToAdapter: JobApplyToAdapter
    lateinit var viewModelJobApplyToViewModel: JobApplyToViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobApplyToViewBinding.inflate(inflater, container, false)

        viewModelJobApplyToViewModel = ViewModelProvider(this).get(JobApplyToViewModel::class.java)
        binding.recyclerViewJobApplyTo.layoutManager = LinearLayoutManager(requireContext())
        setObserverForError()
        setObserverForGetJobAppliedTo()

        binding.buttonSaveJobApplyTo.setOnClickListener {
            setObserverForApplyJobTo()
        }

        return binding.root
    }

    private fun getUsers(appliedUsers: Collection<String>){
        viewModelJobApplyToViewModel.userResult.observe(viewLifecycleOwner){
            jobApplyToAdapter = JobApplyToAdapter(appliedUsers = appliedUsers)
            binding.recyclerViewJobApplyTo.adapter = jobApplyToAdapter
            jobApplyToAdapter.setupData(it.collection.toList())
        }
        viewModelJobApplyToViewModel.getUsers()
    }

    private fun setObserverForGetJobAppliedTo(){
        viewModelJobApplyToViewModel.jobAppliedToRequestResult.observe(viewLifecycleOwner){
            getUsers(it.jobAppliedTo)
        }
        viewModelJobApplyToViewModel.getJobAppliedTo(arguments?.getString("jobId")!!)
    }

    private fun setObserverForApplyJobTo(){
        viewModelJobApplyToViewModel.jobResult.observe(viewLifecycleOwner){
            parentFragmentManager.popBackStack()
        }
        viewModelJobApplyToViewModel.addJobApplyTo(
            objectId = arguments?.getString("jobId")!!,
            jobAppliedTo = jobApplyToAdapter.getCheckedUsers()
        )
    }

    private fun setObserverForError() {
        viewModelJobApplyToViewModel.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelJobApplyToViewModel.errorResult.value = false
            }
        }
    }

}