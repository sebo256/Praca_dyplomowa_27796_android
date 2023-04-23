package com.praca.dyplomowa.android.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentCalendarJobListViewBinding
import com.praca.dyplomowa.android.utils.*
import com.praca.dyplomowa.android.viewmodels.CalendarJobListViewModel
import com.praca.dyplomowa.android.views.adapters.JobAdapter

class CalendarJobListFragmentView : Fragment() {

    private var _binding: FragmentCalendarJobListViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelCalendarJobList: CalendarJobListViewModel
    private lateinit var jobAdapter: JobAdapter
    private lateinit var dateRange: DateRange


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarJobListViewBinding.inflate(inflater, container, false)

        dateRange = Gson().fromJson(arguments?.getString("dateRange")!!, DateRange::class.java)
        viewModelCalendarJobList = ViewModelProvider(this).get(CalendarJobListViewModel::class.java)
        setObserverForJobByLongDateBetween()
        setObserverForDeleteJob()
        setObserverForError()

        binding.textViewDateCalendarJobListActivityView.setText(viewModelCalendarJobList.calculateDate(dateRange.startLong+1))
        binding.recyclerViewCalendarJobListActivity.layoutManager = LinearLayoutManager(requireContext())
        jobAdapter = JobAdapter(recyclerViewJobsUtilsInterface)
        binding.recyclerViewCalendarJobListActivity.adapter = jobAdapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(parentFragmentManager.backStackEntryCount == 1){
                    val fragment = parentFragmentManager.findFragmentByTag("CalendarFragmentView") as CalendarFragmentView
                    fragment.getJobDateAndInfo()
                    parentFragmentManager.popBackStack()
                }else if(parentFragmentManager.backStackEntryCount > 1){
                    parentFragmentManager.popBackStack()
                }else{
                    requireActivity().finish()
                }
            }
        })

        return binding.root
    }

    private fun setObserverForJobByLongDateBetween(){
        viewModelCalendarJobList.jobResult.observe(viewLifecycleOwner){
            jobAdapter.setupData(it.collection.toList())
        }
        getJobsAndUpdateRecyclerData()
    }

    private fun setObserverForDeleteJob(){
        viewModelCalendarJobList.jobDeleteResult.observe(viewLifecycleOwner){
            getJobsAndUpdateRecyclerData()
        }
    }

    private fun setObserverForError() {
        viewModelCalendarJobList.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelCalendarJobList.errorResult.value = false
            }
        }
    }

    fun getJobsAndUpdateRecyclerData(){
        viewModelCalendarJobList.getJobByLongDateBetween(startLong = dateRange.startLong, endLong = dateRange.endLong)
    }

    private val recyclerViewJobsUtilsInterface: RecyclerViewJobsUtilsInterface = object :
        RecyclerViewJobsUtilsInterface {
        override fun onClick(string: String) {
            FragmentNavigationUtils.addFragmentFadeWithOneStringBundleValueAndSourceFragment(
                fragmentManager = parentFragmentManager,
                fragment = JobAddFragmentView(),
                argumentKey = "jobObjectId",
                argumentValue = string,
                argumentSourceFragmentName = "CalendarJobListFragmentView"
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
                            argumentSourceFragmentName = "CalendarJobListFragmentView"
                        )
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
    }

}