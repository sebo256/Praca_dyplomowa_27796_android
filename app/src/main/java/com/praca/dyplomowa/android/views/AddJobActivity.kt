package com.praca.dyplomowa.android.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.request.JobRequest
import com.praca.dyplomowa.android.api.request.JobRequestUpdate
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.databinding.ActivityAddJobBinding
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.AddJobsViewModel


class AddJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJobBinding
    lateinit var viewModelAddJobs: AddJobsViewModel
    private var dateLong: Long? = null
    private var dateHour: Int? = 0
    private var dateMinute: Int? = 0
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(R.string.datepicker_textfield_text)
        .build()
    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setTitleText(R.string.timepicker_textfield_text)
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelAddJobs = ViewModelProvider(this).get(AddJobsViewModel::class.java)

        if(checkIfIntentIsNull()){
            setupForm()
        } else {
            getDataToFillForm()
        }

    }

    private fun setupForm(){
        binding.textFieldPlannedDateJobAddActivity.setOnClickListener {
            datePicker.show(supportFragmentManager, "plannedDate")
        }
        binding.textFieldPlannedTimeJobAddActivity.setOnClickListener {
            timePicker.show(supportFragmentManager, "plannedTime")
        }

        datePicker.addOnPositiveButtonClickListener {
            binding.textFieldPlannedDateJobAddActivity.setText(viewModelAddJobs.calculateSimpleDateFromLong(datePicker.selection!!))
            dateLong = datePicker.selection
        }
        timePicker.addOnPositiveButtonClickListener {
            binding.textFieldPlannedTimeJobAddActivity.setText(String.format("%02d:%02d",timePicker.hour, timePicker.minute))
            dateHour = timePicker.hour
            dateMinute = timePicker.minute
        }

        binding.checkboxIsCompletedJobAddActivity.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                binding.textFieldLayoutTimeSpentJobAddActivity.isEnabled = true
                binding.textFieldLayoutTimeSpentJobAddActivity.helperText = getString(R.string.required_text_label)
            }else{
                binding.textFieldLayoutTimeSpentJobAddActivity.isEnabled = false
                binding.textFieldTimeSpentJobAddActivity.text = null
                binding.textFieldLayoutTimeSpentJobAddActivity.helperText = null
            }
        }

        binding.buttonSaveJobJobAddActivity.setOnClickListener {
            if(validateAddJobData(
                    subject = binding.textFieldSubjectJobAddActivity.text.toString(),
                    jobType = binding.textFieldDropdownJobTypeJobAddActivity.text.toString(),
                    name = binding.textFieldNameJobAddActivity.text.toString(),
                    surname = binding.textFieldSurnameJobAddActivity.text.toString(),
                    street = binding.textFieldStreetJobAddActivity.text.toString(),
                    city = binding.textFieldCityJobAddActivity.text.toString(),
                    timeSpent = binding.textFieldTimeSpentJobAddActivity.text.toString().toIntOrNull() ?: 0
                )) {
                assignUsersDialog()
            }
        }
    }

    private fun fillForm(jobGetAllResponse: JobGetAllResponse){
        dateLong = jobGetAllResponse.plannedDate
        dateHour = viewModelAddJobs.calculateHourFromLong(jobGetAllResponse.plannedDate)
        dateMinute = viewModelAddJobs.calculateMinutesFromLong(jobGetAllResponse.plannedDate)
        binding.textFieldSubjectJobAddActivity.setText(jobGetAllResponse.subject)
        binding.textFieldDropdownJobTypeJobAddActivity.setText(jobGetAllResponse.jobType, false)
        binding.textFieldCompanyNameJobAddActivity.setText(jobGetAllResponse.companyName)
        binding.textFieldNameJobAddActivity.setText(jobGetAllResponse.name)
        binding.textFieldSurnameJobAddActivity.setText(jobGetAllResponse.surname)
        binding.textFieldStreetJobAddActivity.setText(jobGetAllResponse.street)
        binding.textFieldPostalCodeJobAddActivity.setText(jobGetAllResponse.postalCode)
        binding.textFieldCityJobAddActivity.setText(jobGetAllResponse.city)
        binding.textFieldPhoneNumberJobAddActivity.setText(jobGetAllResponse.phoneNumber)
        binding.textFieldEmailJobAddActivity.setText(jobGetAllResponse.email)
        binding.textFieldNoteJobAddActivity.setText(jobGetAllResponse.note)
        binding.textFieldPlannedDateJobAddActivity.setText(viewModelAddJobs.calculateSimpleDateFromLong(dateLong))
        binding.textFieldPlannedTimeJobAddActivity.setText(String.format("%02d:%02d",dateHour, dateMinute))
        binding.checkboxIsCompletedJobAddActivity.isChecked = jobGetAllResponse.isCompleted
        if ( binding.checkboxIsCompletedJobAddActivity.isChecked) binding.textFieldTimeSpentJobAddActivity.setText(jobGetAllResponse.timeSpent)
        setupForm()
    }

    private fun validateAddJobData(subject: String, jobType: String, name: String, surname: String, street: String, city: String, timeSpent: Int): Boolean = listOf(
        validateAddJobDataNullOrBlanks(subject, binding.textFieldLayoutSubjectJobAddActivity),
        validateJobType(jobType, binding.textFieldLayoutJobTypeJobAddActivity),
        validateAddJobDataNameOrSurnameWithBlanks(name, binding.textFieldLayoutNameJobAddActivity),
        validateAddJobDataNameOrSurnameWithBlanks(surname, binding.textFieldLayoutSurnameJobAddActivity),
        validateAddJobDataNullOrBlanks(street, binding.textFieldLayoutStreetJobAddActivity),
        validateAddJobDataNullOrBlanks(city, binding.textFieldLayoutCityJobAddActivity),
        validateAddJobDataTimeSpent(timeSpent, binding.textFieldLayoutTimeSpentJobAddActivity),
    ).all { it }

    private fun validateAddJobDataNullOrBlanks(data: String, field: TextInputLayout): Boolean{
        return if(data.isNullOrBlank()){
            field.error = getString(R.string.error_empty_info)
            field.helperText = null
            binding.scrollViewJobAddActivity.scrollTo(field.top,field.top)
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
            binding.scrollViewJobAddActivity.scrollTo(field.top,field.top)
            false
        }else{
            field.error = getString(R.string.error_name_surname_validator_info)
            field.helperText = null
            binding.scrollViewJobAddActivity.scrollTo(field.top,field.top)
            false
        }
    }

    private fun validateAddJobDataTimeSpent(data: Int, field: TextInputLayout): Boolean {
        return if(data == 0 && binding.checkboxIsCompletedJobAddActivity.isChecked){
            field.error = getString(R.string.error_timeSpentZero_info)
            field.helperText = null
            binding.scrollViewJobAddActivity.scrollTo(field.top, field.top)
            false
        }else if(data != 0 && binding.checkboxIsCompletedJobAddActivity.isChecked){
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
            binding.scrollViewJobAddActivity.scrollTo(field.top, field.top)
            false
        }else{
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }
    }

    private fun addOrUpdateJob(){
        viewModelAddJobs.jobResult.observe(this) {
            print(it)
            finish()
        }
        when(checkIfIntentIsNull()){
            true -> viewModelAddJobs.addJob(getAllDataFromForm())
            false -> viewModelAddJobs.updateJob(getDataForUpdateJob())
        }
    }

    private fun addOrUpdateJobAndGoToJobApplyTo(){
        viewModelAddJobs.jobResult.observe(this){
            print(it)
            val intent = Intent(this, JobApplyToActivityView::class.java)
            intent.putExtra("jobId",it.id)
            startActivity(intent)
            finish()
        }
        when(checkIfIntentIsNull()) {
            true -> viewModelAddJobs.addJob(getAllDataFromForm())
            false -> viewModelAddJobs.updateJob(getDataForUpdateJob())
        }

    }

    private fun getDataToFillForm() {
        viewModelAddJobs.jobGetByIdResult.observe(this){
            fillForm(it)
        }
        viewModelAddJobs.getJobById(intent.getStringExtra("jobObjectId")!!)
    }

    fun getPlannedDate(): Long?{
        var plannedDate: Long? = null
        return if(!binding.textFieldPlannedDateJobAddActivity.text.isNullOrBlank()) {
            plannedDate = viewModelAddJobs.calculatePlannedDate(dateLong!!, dateHour!!, dateMinute!!)
            plannedDate
        }else {
            plannedDate
        }
    }

    fun getAllDataFromForm() =
        JobRequest(
            companyName = binding.textFieldCompanyNameJobAddActivity.text.toString(),
            name = binding.textFieldNameJobAddActivity.text.toString(),
            surname = binding.textFieldSurnameJobAddActivity.text.toString(),
            street = binding.textFieldStreetJobAddActivity.text.toString(),
            postalCode = binding.textFieldPostalCodeJobAddActivity.text.toString(),
            city = binding.textFieldCityJobAddActivity.text.toString(),
            phoneNumber = binding.textFieldPhoneNumberJobAddActivity.text.toString(),
            email = binding.textFieldSubjectJobAddActivity.text.toString(),
            subject = binding.textFieldSubjectJobAddActivity.text.toString(),
            jobType = binding.textFieldDropdownJobTypeJobAddActivity.text.toString(),
            dateOfCreation = System.currentTimeMillis(),
            plannedDate = getPlannedDate(),
            timeSpent = binding.textFieldTimeSpentJobAddActivity.text.toString().toIntOrNull() ?: 0,
            note = binding.textFieldNoteJobAddActivity.text.toString(),
            isCompleted = binding.checkboxIsCompletedJobAddActivity.isChecked,
            createdBy = SessionManager.getCurrentUserId(this)!!
        )

    fun getDataForUpdateJob() =
        JobRequestUpdate(
            objectId = getObjectIdFromIntent(),
            companyName = binding.textFieldCompanyNameJobAddActivity.text.toString(),
            name = binding.textFieldNameJobAddActivity.text.toString(),
            surname = binding.textFieldSurnameJobAddActivity.text.toString(),
            street = binding.textFieldStreetJobAddActivity.text.toString(),
            postalCode = binding.textFieldPostalCodeJobAddActivity.text.toString(),
            city = binding.textFieldCityJobAddActivity.text.toString(),
            phoneNumber = binding.textFieldPhoneNumberJobAddActivity.text.toString(),
            email = binding.textFieldEmailJobAddActivity.text.toString(),
            subject = binding.textFieldSubjectJobAddActivity.text.toString(),
            jobType = binding.textFieldDropdownJobTypeJobAddActivity.text.toString(),
            plannedDate = getPlannedDate(),
            timeSpent = binding.textFieldTimeSpentJobAddActivity.text.toString().toIntOrNull() ?: 0,
            note = binding.textFieldNoteJobAddActivity.text.toString(),
            isCompleted = binding.checkboxIsCompletedJobAddActivity.isChecked,
        )

    fun assignUsersDialog(){
        MaterialAlertDialogBuilder(this)
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

    private fun checkIfIntentIsNull(): Boolean =
        intent.getStringExtra("jobObjectId").isNullOrBlank()

    private fun getObjectIdFromIntent(): String =
        intent.getStringExtra("jobObjectId").toString()

}