package com.praca.dyplomowa.android.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.request.JobRequest
import com.praca.dyplomowa.android.api.request.JobRequestUpdate
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.api.response.JobGetForListResponse
import com.praca.dyplomowa.android.api.response.JobTypeGetAllResponse
import com.praca.dyplomowa.android.databinding.FragmentJobAddViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.FragmentNavigationUtils
import com.praca.dyplomowa.android.utils.RecyclerViewJobsUtilsInterface
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.AddJobsViewModel
import com.praca.dyplomowa.android.views.adapters.JobAddJobTypeAdapter

class JobAddFragmentView : Fragment() {

    private var _binding: FragmentJobAddViewBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModelAddJobs: AddJobsViewModel
    private var dateLong: Long? = null
    private var jobTypeId: String? = null
    private var clientId: String? = null
    private var jobTypes: MutableList<JobTypeGetAllResponse> = ArrayList()


    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(R.string.datepicker_textfield_text)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken,0)
        view.requestFocus()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobAddViewBinding.inflate(inflater, container, false)
        viewModelAddJobs = ViewModelProvider(this).get(AddJobsViewModel::class.java)
        setObserverForError()
        setObserverForGetJobTypes()
        setClientFragmentResultListener()

        when(checkIfArgumentIsNull()){
            true -> setupForm()
            false -> getDataToFillForm()
        }

        return binding.root
    }
    private fun setupForm(){
        binding.textFieldPlannedDateJobAddFragment.setOnClickListener {
            datePicker.show(parentFragmentManager, "plannedDate")
        }

        binding.textFieldClientJobAddFragment.setOnClickListener {
            FragmentNavigationUtils.addFragmentOpenWithSourceFragment(
                fragmentManager = parentFragmentManager,
                fragment = JobClientsFragmentView(),
                argumentSourceFragmentName = "JobAddFragmentView"
            )
        }

        datePicker.addOnPositiveButtonClickListener {
            binding.textFieldPlannedDateJobAddFragment.setText(viewModelAddJobs.calculateSimpleDateFromLong(datePicker.selection!!))
            dateLong = datePicker.selection
        }

        binding.checkboxIsCompletedJobAddFragment.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                binding.textFieldLayoutTimeSpentJobAddFragment.isEnabled = true
                binding.textFieldLayoutTimeSpentJobAddFragment.helperText = getString(R.string.required_text_label)
            }else{
                binding.textFieldLayoutTimeSpentJobAddFragment.isEnabled = false
                binding.textFieldTimeSpentJobAddFragment.text = null
                binding.textFieldLayoutTimeSpentJobAddFragment.helperText = null
            }
        }

        binding.buttonSaveJobJobAddFragment.setOnClickListener {
            if(validateAddJobData(
                    subject = binding.textFieldSubjectJobAddFragment.text.toString(),
                    jobType = binding.textFieldDropdownJobTypeJobAddFragment.text.toString(),
                    client = binding.textFieldClientJobAddFragment.text.toString(),
                    timeSpent = binding.textFieldTimeSpentJobAddFragment.text.toString().toIntOrNull() ?: 0
                )) {
                assignUsersDialog()
            }
        }

    }

    private fun fillForm(jobGetAllResponse: JobGetAllResponse){
        dateLong = jobGetAllResponse.plannedDate
        jobTypeId = jobGetAllResponse.jobType.id
        clientId = jobGetAllResponse.client.id
        binding.textFieldSubjectJobAddFragment.setText(jobGetAllResponse.subject)
        binding.textFieldDropdownJobTypeJobAddFragment.setText(jobGetAllResponse.jobType.jobType, false)
        when(jobGetAllResponse.client.companyName.isNullOrBlank()){
            true -> binding.textFieldClientJobAddFragment.setText(
                jobGetAllResponse.client.name + " " + jobGetAllResponse.client.surname
            )
            false -> binding.textFieldClientJobAddFragment.setText(
                jobGetAllResponse.client.companyName + ", " + jobGetAllResponse.client.name + " " + jobGetAllResponse.client.surname
            )
        }
        binding.textFieldNoteJobAddFragment.setText(jobGetAllResponse.note)
        binding.textFieldPlannedDateJobAddFragment.setText(viewModelAddJobs.calculateSimpleDateFromLong(dateLong))
        binding.checkboxIsCompletedJobAddFragment.isChecked = jobGetAllResponse.isCompleted
        if ( binding.checkboxIsCompletedJobAddFragment.isChecked || jobGetAllResponse.isCompleted) {
            binding.textFieldTimeSpentJobAddFragment.isEnabled = true
            binding.textFieldTimeSpentJobAddFragment.setText(jobGetAllResponse.timeSpent.toString())
        }
        setupForm()
    }

    private fun validateAddJobData(subject: String, jobType: String, client: String, timeSpent: Int): Boolean = listOf(
        validateAddJobDataNullOrBlanks(subject, binding.textFieldLayoutSubjectJobAddFragment),
        validateJobType(jobType, binding.textFieldLayoutJobTypeJobAddFragment),
        validateAddJobDataClientWithBlanks(client, binding.textFieldLayoutClientJobAddFragment),
        validateAddJobDataTimeSpent(timeSpent, binding.textFieldLayoutTimeSpentJobAddFragment),
    ).all { it }

    private fun validateAddJobDataNullOrBlanks(data: String, field: TextInputLayout): Boolean{
        return if(data.isNullOrBlank()){
            field.error = getString(R.string.error_empty_info)
            field.helperText = null
            false
        }else{
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }
    }

    private fun validateAddJobDataClientWithBlanks(data: String, field: TextInputLayout): Boolean {
        return if(!data.isNullOrBlank() && !clientId.isNullOrBlank()) {
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }else{
            field.error = getString(R.string.error_empty_info)
            field.helperText = null
            false
        }
    }

    private fun validateAddJobDataTimeSpent(data: Int, field: TextInputLayout): Boolean {
        return if(data == 0 && binding.checkboxIsCompletedJobAddFragment.isChecked){
            field.error = getString(R.string.error_timeSpentZero_info)
            field.helperText = null
            false
        }else if(data != 0 && binding.checkboxIsCompletedJobAddFragment.isChecked){
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }else{
            true
        }
    }

    private fun validateJobType(data: String, field: TextInputLayout): Boolean {
        return if(data.isNullOrBlank()){
            field.error = getString(R.string.error_empty_info)
            field.helperText = null
            false
        }else{
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }
    }

    private fun addOrUpdateJob(){
        viewModelAddJobs.jobResult.observe(requireActivity()) {
            updateRecyclerData()
            parentFragmentManager.popBackStack()
        }
        when(checkIfArgumentIsNull()){
            true -> viewModelAddJobs.addJob(getAllDataFromForm())
            false -> viewModelAddJobs.updateJob(getDataForUpdateJob())
        }
    }

    private fun addOrUpdateJobAndGoToJobApplyTo(){
        viewModelAddJobs.jobResult.observe(viewLifecycleOwner){
            updateRecyclerData()

            parentFragmentManager.popBackStack()
            FragmentNavigationUtils.replaceFragmentWithOneStringBundleValue(
                fragmentManager = parentFragmentManager,
                fragment = JobApplyToFragmentView(),
                argumentKey = "jobId",
                argumentValue = it.id
            )

        }
        when(checkIfArgumentIsNull()) {
            true -> viewModelAddJobs.addJob(getAllDataFromForm())
            false -> viewModelAddJobs.updateJob(getDataForUpdateJob())
        }
    }

    private fun updateRecyclerData(){
        val sourceFragmentName = arguments?.getString("argumentSourceFragmentName")
        if(sourceFragmentName == "JobsFragmentView"){
            val fragment = parentFragmentManager.findFragmentByTag("JobsFragmentView") as JobsFragmentView
            fragment.getJobs()
        }else if(sourceFragmentName == "CalendarJobListFragmentView"){
            val fragment = parentFragmentManager.findFragmentByTag("CalendarJobListFragmentView") as CalendarJobListFragmentView
            fragment.getJobsAndUpdateRecyclerData()
        }else if(sourceFragmentName == "ProfileJobListFragmentView"){
            val fragment = parentFragmentManager.findFragmentByTag("ProfileJobListFragmentView") as ProfileJobListFragmentView
            fragment.getJobsAndUpdateRecyclerData()
        }
    }

    private fun getDataToFillForm() {
        viewModelAddJobs.jobGetByIdResult.observe(viewLifecycleOwner){
            fillForm(it)
        }
        viewModelAddJobs.getJobById(arguments?.getString("jobObjectId")!!)
    }

    private fun setObserverForError() {
        viewModelAddJobs.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelAddJobs.errorResult.value = false
            }
        }
    }

    private fun setObserverForGetJobTypes(){
        viewModelAddJobs.jobTypeResult.observe(viewLifecycleOwner){
            jobTypes = it.collection.toMutableList()
            binding.textFieldDropdownJobTypeJobAddFragment.setAdapter(JobAddJobTypeAdapter(
                context = requireContext(),
                layout = R.layout.dropdown_job_types_layout,
                list = jobTypes,
                recyclerViewJobsUtilsInterface = recyclerViewJobsUtilsInterface
            ))
        }
        viewModelAddJobs.getJobTypes()
    }

    fun getAllDataFromForm() =
        JobRequest(
            client = clientId!!,
            subject = binding.textFieldSubjectJobAddFragment.text.toString(),
            jobType = jobTypeId!!,
            dateOfCreation = System.currentTimeMillis(),
            plannedDate = dateLong,
            timeSpent = binding.textFieldTimeSpentJobAddFragment.text.toString().toIntOrNull() ?: 0,
            note = binding.textFieldNoteJobAddFragment.text.toString(),
            isCompleted = binding.checkboxIsCompletedJobAddFragment.isChecked,
            createdBy = SessionManager.getCurrentUserUsername(requireContext())!!
        )

    fun getDataForUpdateJob() =
        JobRequestUpdate(
            objectId = getObjectIdFromArgument(),
            client = clientId!!,
            subject = binding.textFieldSubjectJobAddFragment.text.toString(),
            jobType = jobTypeId!!,
            plannedDate = dateLong,
            timeSpent = binding.textFieldTimeSpentJobAddFragment.text.toString().toIntOrNull() ?: 0,
            note = binding.textFieldNoteJobAddFragment.text.toString(),
            isCompleted = binding.checkboxIsCompletedJobAddFragment.isChecked,
        )

    fun assignUsersDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_text_title)
            .setMessage(R.string.dialog_message_title)
            .setPositiveButton(R.string.dialog_addjob_positive_title) { dialog, which ->
                addOrUpdateJob()
            }
            .setNeutralButton(R.string.dialog_addjob_neutral_title) { dialog, which ->
                addOrUpdateJobAndGoToJobApplyTo()
            }
            .setNegativeButton(R.string.dialog_addjob_negative_title) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setClientFragmentResultListener(){
        parentFragmentManager.setFragmentResultListener("pickedClientResult", viewLifecycleOwner) { requestKey, bundle ->
            val clientData = Gson().fromJson(bundle.getString("pickedClient"), JobGetForListResponse::class.java)
            clientId = clientData.id
            when(clientData.companyName.isNullOrBlank()){
                true -> binding.textFieldClientJobAddFragment.setText(
                    clientData.name + " " + clientData.surname
                )
                false -> binding.textFieldClientJobAddFragment.setText(
                    clientData.companyName + ", " + clientData.name + " " + clientData.surname
                )
            }
        }
    }

    private fun checkIfArgumentIsNull(): Boolean =
        arguments?.getString("jobObjectId").isNullOrBlank()

    private fun getObjectIdFromArgument(): String =
        arguments?.getString("jobObjectId").toString()

    private val recyclerViewJobsUtilsInterface: RecyclerViewJobsUtilsInterface = object :
        RecyclerViewJobsUtilsInterface {
        override fun onClick(stringFirst: String, stringSecond: String) {
            binding.textFieldDropdownJobTypeJobAddFragment.setText(stringFirst, false)
            jobTypeId = stringSecond
            binding.textFieldDropdownJobTypeJobAddFragment.dismissDropDown()
        }
    }
}