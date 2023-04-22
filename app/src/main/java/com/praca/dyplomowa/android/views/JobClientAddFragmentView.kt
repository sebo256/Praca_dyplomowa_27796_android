package com.praca.dyplomowa.android.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.api.request.ClientRequest
import com.praca.dyplomowa.android.api.request.ClientRequestUpdate
import com.praca.dyplomowa.android.api.response.ClientGetAllResponse
import com.praca.dyplomowa.android.databinding.FragmentJobClientAddViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.viewmodels.JobClientAddViewModel

class JobClientAddFragmentView : Fragment() {

    private var _binding: FragmentJobClientAddViewBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModelJobClientAdd: JobClientAddViewModel

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
        _binding = FragmentJobClientAddViewBinding.inflate(inflater, container, false)

        viewModelJobClientAdd = ViewModelProvider(this).get(JobClientAddViewModel::class.java)
        setObserverForError()
        setObserverForAddOrUpdateClient()
        setObserverForGetClientById()
        setupForm()

        if(!checkIfArgumentIsNull()){
            viewModelJobClientAdd.getClientById(getObjectIdFromArgument())
        }



        return binding.root
    }

    private fun setObserverForError() {
        viewModelJobClientAdd.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelJobClientAdd.errorResult.value = false
            }
        }
    }

    private fun setObserverForAddOrUpdateClient() {
        viewModelJobClientAdd.clientAddOrUpdateResult.observe(viewLifecycleOwner){
            when(it.status){
                true -> updateRecyclerData()
                false -> ErrorDialogHandler(requireContext())
            }
        }
    }

    private fun setObserverForGetClientById(){
        viewModelJobClientAdd.clientResult.observe(viewLifecycleOwner){
            fillFormWithData(it)
        }
    }

    private fun setupForm() {

        binding.buttonAddClientAddClientFragment.setOnClickListener {
            if(validateAddClientData(
                    name = binding.textFieldNameClientAddFragment.text.toString(),
                    surname = binding.textFieldSurnameClientAddFragment.text.toString(),
                    street = binding.textFieldStreetClientAddFragment.text.toString(),
                    postalCode = binding.textFieldPostalCodeClientAddFragment.text.toString(),
                    city = binding.textFieldCityClientAddFragment.text.toString()
            )){
                addOrUpdateClient()
            }
        }

    }

    private fun fillFormWithData(data: ClientGetAllResponse) {
        binding.textFieldCompanyNameClientAddFragment.setText(data.companyName)
        binding.textFieldNameClientAddFragment.setText(data.name)
        binding.textFieldSurnameClientAddFragment.setText(data.surname)
        binding.textFieldStreetClientAddFragment.setText(data.street)
        binding.textFieldPostalCodeClientAddFragment.setText(data.postalCode)
        binding.textFieldCityClientAddFragment.setText(data.city)
        binding.textFieldPhoneNumberClientAddFragment.setText(data.phoneNumber)
        binding.textFieldEmailClientAddFragment.setText(data.email)
    }

    private fun addOrUpdateClient(){
        when(checkIfArgumentIsNull()){
            true -> viewModelJobClientAdd.addClient(getAllDataFromForm())
            false -> viewModelJobClientAdd.updateClient(getAllDataFromForForUpdate())
        }
    }

    private fun getAllDataFromForm() =
        ClientRequest(
            companyName = binding.textFieldCompanyNameClientAddFragment.text.toString(),
            name = binding.textFieldNameClientAddFragment.text.toString(),
            surname = binding.textFieldSurnameClientAddFragment.text.toString(),
            street = binding.textFieldStreetClientAddFragment.text.toString(),
            postalCode = binding.textFieldPostalCodeClientAddFragment.text.toString(),
            city = binding.textFieldCityClientAddFragment.text.toString(),
            phoneNumber = binding.textFieldPhoneNumberClientAddFragment.text.toString(),
            email = binding.textFieldEmailClientAddFragment.text.toString()
        )

    private fun getAllDataFromForForUpdate() =
        ClientRequestUpdate(
            objectId = getObjectIdFromArgument(),
            companyName = binding.textFieldCompanyNameClientAddFragment.text.toString(),
            name = binding.textFieldNameClientAddFragment.text.toString(),
            surname = binding.textFieldSurnameClientAddFragment.text.toString(),
            street = binding.textFieldStreetClientAddFragment.text.toString(),
            postalCode = binding.textFieldPostalCodeClientAddFragment.text.toString(),
            city = binding.textFieldCityClientAddFragment.text.toString(),
            phoneNumber = binding.textFieldPhoneNumberClientAddFragment.text.toString(),
            email = binding.textFieldEmailClientAddFragment.text.toString()
        )

    private fun updateRecyclerData() {
        val fragment = parentFragmentManager.findFragmentByTag("JobClientsFragmentView") as JobClientsFragmentView
        fragment.getClients()
        parentFragmentManager.popBackStack()
    }

    private fun validateAddClientData(name: String, surname: String, street: String, postalCode: String, city: String): Boolean = listOf(
        validateAddClientDataNameOrSurnameWithBlanks(name, binding.textFieldLayoutNameClientAddFragment),
        validateAddClientDataNameOrSurnameWithBlanks(surname, binding.textFieldLayoutSurnameClientAddFragment),
        validateClientDataNullOrBlanks(street, binding.textFieldLayoutStreetClientAddFragment),
        validateClientPostalCodeWithBlanks(postalCode, binding.textFieldLayoutPostalCodeClientAddFragment),
        validateClientDataNullOrBlanks(city, binding.textFieldLayoutCityClientAddFragment)
    ).all { it }

    private fun validateAddClientDataNameOrSurnameWithBlanks(data: String, field: TextInputLayout): Boolean {
        val NAME_PATTERN = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}".toRegex()
        return if(data.length in 3 .. 25 && data.contains(NAME_PATTERN)){
            field.error = null
            true
        }else if(data.isNullOrBlank()){
            field.error = getString(R.string.error_empty_info)
            false
        }else{
            field.error = getString(R.string.error_name_surname_validator_info)
            false
        }
    }

    private fun validateClientDataNullOrBlanks(data: String, field: TextInputLayout): Boolean{
        return if(data.isNullOrBlank()){
            field.error = getString(R.string.error_empty_info)
            false
        }else{
            field.error = null
            true
        }
    }

    private fun validateClientPostalCodeWithBlanks(data: String, field: TextInputLayout): Boolean {
        val postalCodePattern = "\\d\\d-\\d\\d\\d".toRegex()
        return if(data.matches(postalCodePattern)){
            field.error = null
            true
        }else if(data.isNullOrBlank()){
            field.error = getString(R.string.error_short_empty_info)
            false
        } else {
            field.error = getString(R.string.error_postalcode_validator_info)
            false
        }
    }

    private fun checkIfArgumentIsNull(): Boolean =
        arguments?.getString("clientObjectId").isNullOrBlank()

    private fun getObjectIdFromArgument(): String =
        arguments?.getString("clientObjectId").toString()

}