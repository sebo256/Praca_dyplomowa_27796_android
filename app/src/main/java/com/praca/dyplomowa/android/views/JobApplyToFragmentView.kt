package com.praca.dyplomowa.android.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.request.JobAddTimeSpentRequest
import com.praca.dyplomowa.android.api.response.JobAppliedToResponse
import com.praca.dyplomowa.android.databinding.FragmentJobApplyToViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.JobApplyToViewModel
import com.praca.dyplomowa.android.views.adapters.JobApplyToAdapter

class JobApplyToFragmentView : Fragment() {
    private var _binding: FragmentJobApplyToViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobApplyToAdapter: JobApplyToAdapter
    lateinit var viewModelJobApplyToViewModel: JobApplyToViewModel
    private var hoursMap: MutableMap<String, Int> = mutableMapOf()

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
        setObserverForAddTimeSpent()

        binding.buttonSaveJobApplyTo.setOnClickListener {
            setObserverForApplyJobToAndGetHours()
        }
        return binding.root
    }

    private fun getUsers(jobAppliedToResponse: JobAppliedToResponse){
        viewModelJobApplyToViewModel.userResult.observe(viewLifecycleOwner){
            jobApplyToAdapter = JobApplyToAdapter(jobAppliedToResponse)
            binding.recyclerViewJobApplyTo.adapter = jobApplyToAdapter
            jobApplyToAdapter.setupData(it.collection.toList())
        }
        viewModelJobApplyToViewModel.getUsers()
    }

    private fun setObserverForGetJobAppliedTo(){
        viewModelJobApplyToViewModel.jobAppliedToRequestResult.observe(viewLifecycleOwner){
            getUsers(it)
        }
        viewModelJobApplyToViewModel.getJobAppliedTo(arguments?.getString("jobId")!!)
    }

    private fun setObserverForApplyJobTo(){
        viewModelJobApplyToViewModel.jobResult.observe(viewLifecycleOwner){
            if(SessionManager.getIsAdmin(requireContext())){
                viewModelJobApplyToViewModel.addTimeSpent(
                    JobAddTimeSpentRequest(
                        objectId = arguments?.getString("jobId")!!,
                        timeSpentMap = hoursMap
                    )
                )
            }else{
                parentFragmentManager.popBackStack()
            }
        }
        viewModelJobApplyToViewModel.addJobApplyTo(
            objectId = arguments?.getString("jobId")!!,
            jobAppliedTo = jobApplyToAdapter.getCheckedUsers()
        )
    }

    private fun setObserverForAddTimeSpent(){
        viewModelJobApplyToViewModel.addTimeSpentResult.observe(viewLifecycleOwner){
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(requireView().windowToken,0)
            requireView().requestFocus()
            parentFragmentManager.popBackStack()
        }
    }

    private fun setObserverForError() {
        viewModelJobApplyToViewModel.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelJobApplyToViewModel.errorResult.value = false
            }
        }
    }

    private fun setObserverForApplyJobToAndGetHours(){
        var validatorList: MutableList<Boolean> = mutableListOf()
        for(i in jobApplyToAdapter.dataDiffer.currentList.indices){
            if((binding.recyclerViewJobApplyTo.getChildAt(i).findViewById<View>(R.id.checkBoxRecyclerJobApplyTo) as CheckBox).isChecked){
                if((binding.recyclerViewJobApplyTo.getChildAt(i).findViewById<View>(R.id.textFieldTextTimeJobApplyToFragment) as EditText).text.isNullOrEmpty()){
                    validatorList.add(
                        validateHoursField(
                             (binding.recyclerViewJobApplyTo.getChildAt(i).findViewById<View>(R.id.textFieldTextTimeJobApplyToFragment) as EditText).text.toString(),
                             (binding.recyclerViewJobApplyTo.getChildAt(i).findViewById<View>(R.id.textFieldLayoutTimeJobApplyToFragment) as TextInputLayout)
                    ))
                }else{
                    hoursMap.put(
                        key = jobApplyToAdapter.dataDiffer.currentList.elementAt(i).username,
                        value = (binding
                            .recyclerViewJobApplyTo
                            .getChildAt(i)
                            .findViewById<View>(R.id.textFieldTextTimeJobApplyToFragment) as EditText)
                            .text.toString().toInt()
                    )
                }
            }
        }
        if(validatorList.all { it }){
            setObserverForApplyJobTo()
        }
    }

    private fun validateHoursField(data: String, field: TextInputLayout): Boolean{
        return if(data.isNullOrBlank()){
            field.error = getString(R.string.error_short_empty_info)
            false
        } else {
            field.error = null
            true
        }
    }

}