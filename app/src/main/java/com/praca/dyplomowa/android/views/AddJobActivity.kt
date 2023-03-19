package com.praca.dyplomowa.android.views

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.response.UserGetAllResponse
import com.praca.dyplomowa.android.api.response.UserGetAllResponseCollection
import com.praca.dyplomowa.android.databinding.ActivityAddJobBinding
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.AddJobsViewModel
import java.util.*

private lateinit var binding: ActivityAddJobBinding
lateinit var viewModelAddJobs: AddJobsViewModel

class AddJobActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupForm()
        viewModelAddJobs = ViewModelProvider(this).get(AddJobsViewModel::class.java)

    }

    private fun setupForm(){
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.datepicker_textfield_text)
            .build()
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText(R.string.timepicker_textfield_text)
            .build()


        binding.textFieldPlannedDateJobAddActivity.setOnClickListener { datePicker.show(supportFragmentManager, "plannedDate") }
        binding.textFieldPlannedTimeJobAddActivity.setOnClickListener { timePicker.show(supportFragmentManager, "plannedTime") }

        datePicker.addOnPositiveButtonClickListener {
            binding.textFieldPlannedDateJobAddActivity.setText(datePicker.headerText.toString())
        }
        timePicker.addOnPositiveButtonClickListener {
            binding.textFieldPlannedTimeJobAddActivity.setText(String.format("%02d:%02d",timePicker.hour, timePicker.minute))
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
            val subject = binding.textFieldSubjectJobAddActivity.text.toString()
            val name = binding.textFieldNameJobAddActivity.text.toString()
            val surname = binding.textFieldSurnameJobAddActivity.text.toString()
            val street = binding.textFieldStreetJobAddActivity.text.toString()
            val city = binding.textFieldCityJobAddActivity.text.toString()
            val timeSpent = binding.textFieldTimeSpentJobAddActivity.text.toString().toIntOrNull() ?: 0
//            assignUsersDialog()
            if(validateAddJobData(subject, name, surname, street, city, timeSpent)) {
                addJob(timePicker, datePicker)
            }
        }
    }

    private fun validateAddJobData(subject: String, name: String, surname: String, street: String, city: String, timeSpent: Int): Boolean = listOf(
        validateAddJobDataNullOrBlanks(subject, binding.textFieldLayoutSubjectJobAddActivity),
        validateAddJobDataNameOrSurnameWithBlanks(name, binding.textFieldLayoutNameJobAddActivity),
        validateAddJobDataNameOrSurnameWithBlanks(surname, binding.textFieldLayoutSurnameJobAddActivity),
        validateAddJobDataNullOrBlanks(street, binding.textFieldLayoutStreetJobAddActivity),
        validateAddJobDataNullOrBlanks(city, binding.textFieldLayoutCityJobAddActivity),
        validateAddJobDataTimeSpent(timeSpent, binding.textFieldLayoutTimeSpentJobAddActivity)
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
            binding.scrollViewJobAddActivity.scrollTo(field.top,field.top)
            false
        }else if(data != 0 && binding.checkboxIsCompletedJobAddActivity.isChecked){
            field.error = null
            field.helperText = getString(R.string.required_text_label)
            true
        }else{
            true
        }
    }

    private fun addJob(timePicker: MaterialTimePicker, datePicker: MaterialDatePicker<Long>){
        val companyName = binding.textFieldCompanyNameJobAddActivity.text.toString()
        val name = binding.textFieldNameJobAddActivity.text.toString()
        val surname = binding.textFieldSurnameJobAddActivity.text.toString()
        val street = binding.textFieldStreetJobAddActivity.text.toString()
        val postalCode = binding.textFieldPostalCodeJobAddActivity.text.toString()
        val city = binding.textFieldCityJobAddActivity.text.toString()
        val phoneNumber = binding.textFieldPhoneNumberJobAddActivity.text.toString()
        val email = binding.textFieldSubjectJobAddActivity.text.toString()
        val subject = binding.textFieldSubjectJobAddActivity.text.toString()
        val dateOfCreation = System.currentTimeMillis()
        var plannedDate: Long? = null
        if(!binding.textFieldPlannedTimeJobAddActivity.text.isNullOrBlank() && !binding.textFieldPlannedDateJobAddActivity.text.isNullOrBlank())
            plannedDate = viewModelAddJobs.calculatePlannedDate(datePicker.selection!!, timePicker.hour, timePicker.minute)
        val timeSpent = binding.textFieldTimeSpentJobAddActivity.text.toString().toIntOrNull() ?: 0
        val note = binding.textFieldNoteJobAddActivity.text.toString()
        val isCompleted = binding.checkboxIsCompletedJobAddActivity.isChecked
        val createdBy = SessionManager.getCurrentUserId(baseContext)
        viewModelAddJobs.jobResult.observe(this){
            print(it)
        }
        viewModelAddJobs.addJob(companyName, name, surname, street, postalCode, city, phoneNumber, email, subject, dateOfCreation, plannedDate, timeSpent, note, isCompleted, createdBy!!)
    }

//    fun assignUsersDialog(){
//        MaterialAlertDialogBuilder(applicationContext)
//            .setTitle(R.string.dialog_text_title)
//            .setPositiveButton(R.string.save_button_text) {dialog, which ->
//                finish()
//            }
//            .show()
//    }
}