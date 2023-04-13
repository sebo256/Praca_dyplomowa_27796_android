package com.praca.dyplomowa.android.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.request.JobRequest
import com.praca.dyplomowa.android.api.request.JobRequestUpdate
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.databinding.FragmentJobAddViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.FragmentNavigationUtils
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.AddJobsViewModel

class JobAddFragmentView : Fragment() {

    private var _binding: FragmentJobAddViewBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModelAddJobs: AddJobsViewModel
    private var dateLong: Long? = null
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(R.string.datepicker_textfield_text)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobAddViewBinding.inflate(inflater, container, false)
        viewModelAddJobs = ViewModelProvider(this).get(AddJobsViewModel::class.java)
        setObserverForError()

        if(checkIfArgumentIsNull()){
            setupForm()
        } else {
            getDataToFillForm()
        }
        return binding.root
    }
    private fun setupForm(){
        binding.textFieldPlannedDateJobAddFragment.setOnClickListener {
            datePicker.show(parentFragmentManager, "plannedDate")
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
                    name = binding.textFieldNameJobAddFragment.text.toString(),
                    surname = binding.textFieldSurnameJobAddFragment.text.toString(),
                    street = binding.textFieldStreetJobAddFragment.text.toString(),
                    city = binding.textFieldCityJobAddFragment.text.toString(),
                    timeSpent = binding.textFieldTimeSpentJobAddFragment.text.toString().toIntOrNull() ?: 0
                )) {
                assignUsersDialog()
            }
        }
    }

    private fun fillForm(jobGetAllResponse: JobGetAllResponse){
        dateLong = jobGetAllResponse.plannedDate
        binding.textFieldSubjectJobAddFragment.setText(jobGetAllResponse.subject)
        binding.textFieldDropdownJobTypeJobAddFragment.setText(jobGetAllResponse.jobType, false)
        binding.textFieldCompanyNameJobAddFragment.setText(jobGetAllResponse.companyName)
        binding.textFieldNameJobAddFragment.setText(jobGetAllResponse.name)
        binding.textFieldSurnameJobAddFragment.setText(jobGetAllResponse.surname)
        binding.textFieldStreetJobAddFragment.setText(jobGetAllResponse.street)
        binding.textFieldPostalCodeJobAddFragment.setText(jobGetAllResponse.postalCode)
        binding.textFieldCityJobAddFragment.setText(jobGetAllResponse.city)
        binding.textFieldPhoneNumberJobAddFragment.setText(jobGetAllResponse.phoneNumber)
        binding.textFieldEmailJobAddFragment.setText(jobGetAllResponse.email)
        binding.textFieldNoteJobAddFragment.setText(jobGetAllResponse.note)
        binding.textFieldPlannedDateJobAddFragment.setText(viewModelAddJobs.calculateSimpleDateFromLong(dateLong))
        binding.checkboxIsCompletedJobAddFragment.isChecked = jobGetAllResponse.isCompleted
        if ( binding.checkboxIsCompletedJobAddFragment.isChecked || jobGetAllResponse.isCompleted) {
            binding.textFieldTimeSpentJobAddFragment.isEnabled = true
            binding.textFieldTimeSpentJobAddFragment.setText(jobGetAllResponse.timeSpent.toString())
        }
        setupForm()
    }

    private fun validateAddJobData(subject: String, jobType: String, name: String, surname: String, street: String, city: String, timeSpent: Int): Boolean = listOf(
        validateAddJobDataNullOrBlanks(subject, binding.textFieldLayoutSubjectJobAddFragment),
        validateJobType(jobType, binding.textFieldLayoutJobTypeJobAddFragment),
        validateAddJobDataNameOrSurnameWithBlanks(name, binding.textFieldLayoutNameJobAddFragment),
        validateAddJobDataNameOrSurnameWithBlanks(surname, binding.textFieldLayoutSurnameJobAddFragment),
        validateAddJobDataNullOrBlanks(street, binding.textFieldLayoutStreetJobAddFragment),
        validateAddJobDataNullOrBlanks(city, binding.textFieldLayoutCityJobAddFragment),
        validateAddJobDataTimeSpent(timeSpent, binding.textFieldLayoutTimeSpentJobAddFragment),
    ).all { it }

    private fun validateAddJobDataNullOrBlanks(data: String, field: TextInputLayout): Boolean{
        return if(data.isNullOrBlank()){
            field.error = getString(R.string.error_empty_info)
            field.helperText = null
            binding.scrollViewJobAddFragment.scrollTo(field.top,field.top)
            false
        }else{
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }
    }

    private fun validateAddJobDataNameOrSurnameWithBlanks(data: String, field: TextInputLayout): Boolean {
        val NAME_PATTERN = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}".toRegex()
        return if(data.length in 3 .. 25 && data.contains(NAME_PATTERN)){
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }else if(data.isNullOrBlank()){
            field.error = getString(R.string.error_empty_info)
            field.helperText = null
            binding.scrollViewJobAddFragment.scrollTo(field.top,field.top)
            false
        }else{
            field.error = getString(R.string.error_name_surname_validator_info)
            field.helperText = null
            binding.scrollViewJobAddFragment.scrollTo(field.top,field.top)
            false
        }
    }

    private fun validateAddJobDataTimeSpent(data: Int, field: TextInputLayout): Boolean {
        return if(data == 0 && binding.checkboxIsCompletedJobAddFragment.isChecked){
            field.error = getString(R.string.error_timeSpentZero_info)
            field.helperText = null
            binding.scrollViewJobAddFragment.scrollTo(field.top, field.top)
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
            binding.scrollViewJobAddFragment.scrollTo(field.top, field.top)
            false
        }else{
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }
    }

    private fun addOrUpdateJob(){
        viewModelAddJobs.jobResult.observe(requireActivity()) {
            parentFragmentManager.popBackStack()
        }
        when(checkIfArgumentIsNull()){
            true -> viewModelAddJobs.addJob(getAllDataFromForm())
            false -> viewModelAddJobs.updateJob(getDataForUpdateJob())
        }
    }

    private fun addOrUpdateJobAndGoToJobApplyTo(){
        viewModelAddJobs.jobResult.observe(viewLifecycleOwner){
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

    fun getAllDataFromForm() =
        JobRequest(
            companyName = binding.textFieldCompanyNameJobAddFragment.text.toString(),
            name = binding.textFieldNameJobAddFragment.text.toString(),
            surname = binding.textFieldSurnameJobAddFragment.text.toString(),
            street = binding.textFieldStreetJobAddFragment.text.toString(),
            postalCode = binding.textFieldPostalCodeJobAddFragment.text.toString(),
            city = binding.textFieldCityJobAddFragment.text.toString(),
            phoneNumber = binding.textFieldPhoneNumberJobAddFragment.text.toString(),
            email = binding.textFieldSubjectJobAddFragment.text.toString(),
            subject = binding.textFieldSubjectJobAddFragment.text.toString(),
            jobType = binding.textFieldDropdownJobTypeJobAddFragment.text.toString(),
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
            companyName = binding.textFieldCompanyNameJobAddFragment.text.toString(),
            name = binding.textFieldNameJobAddFragment.text.toString(),
            surname = binding.textFieldSurnameJobAddFragment.text.toString(),
            street = binding.textFieldStreetJobAddFragment.text.toString(),
            postalCode = binding.textFieldPostalCodeJobAddFragment.text.toString(),
            city = binding.textFieldCityJobAddFragment.text.toString(),
            phoneNumber = binding.textFieldPhoneNumberJobAddFragment.text.toString(),
            email = binding.textFieldEmailJobAddFragment.text.toString(),
            subject = binding.textFieldSubjectJobAddFragment.text.toString(),
            jobType = binding.textFieldDropdownJobTypeJobAddFragment.text.toString(),
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

    private fun checkIfArgumentIsNull(): Boolean =
        arguments?.getString("jobObjectId").isNullOrBlank()

    private fun getObjectIdFromArgument(): String =
        arguments?.getString("jobObjectId").toString()
}