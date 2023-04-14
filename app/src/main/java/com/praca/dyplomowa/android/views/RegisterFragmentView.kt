package com.praca.dyplomowa.android.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.FragmentRegisterViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.viewmodels.RegisterViewModel

class RegisterFragmentView : Fragment() {

    private var _binding: FragmentRegisterViewBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModelRegister: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterViewBinding.inflate(inflater, container, false)

        viewModelRegister = ViewModelProvider(this).get(RegisterViewModel::class.java)
        setObserverForError()
        binding.buttonRegisterRegisterFragment.setOnClickListener { registerUser() }

        binding.buttonBackRegisterFragment.setOnClickListener { parentFragmentManager.popBackStack() }

        return binding.root
    }

    private fun setObserverForError() {
        viewModelRegister.errorResult.observe(viewLifecycleOwner){
            if(it == true) {
                ErrorDialogHandler(requireContext())
                viewModelRegister.errorResult.value = false
            }
        }
    }

    private fun registerUser(){
        val username = binding.textFieldUsernameRegisterFragment.text.toString()
        val password = binding.textFieldPasswordRegisterFragment.text.toString()
        val name = binding.textFieldNameRegisterFragment.text.toString()
        val surname = binding.textFieldSurnameRegisterFragment.text.toString()
        if(validateRegistrationData(username, password, name, surname)){
            viewModelRegister.registerResult.observe(viewLifecycleOwner) {
                if(it.account == null){
                    binding.textFieldLayoutUsernameRegisterFragment.error = getString(R.string.register_error_usernameExists_info)
                }else{
                    parentFragmentManager.popBackStack()
                }
            }
            viewModelRegister.registerUser(username, password, name, surname)
        }
    }

    private fun validateRegistrationData(username: String, password: String, name: String, surname: String): Boolean{
        val PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%]).{8,30}\$".toRegex()
        val USERNAME_PATTERN = "^[a-zA-Z0-9._-]{4,}\$".toRegex()
        val NAME_PATTERN = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}".toRegex()
        var validator: Boolean

        if(name.length in 3 .. 25 && name.contains(NAME_PATTERN)){
            validator = true
            binding.textFieldLayoutNameRegisterFragment.error = null
        }else{
            validator = false
            binding.textFieldLayoutNameRegisterFragment.error = getString(R.string.register_error_name_info)
        }

        if(surname.length in 4 .. 25 && surname.contains(NAME_PATTERN)){
            validator = true
            binding.textFieldLayoutSurnameRegisterFragment.error = null
        }else{
            validator = false
            binding.textFieldLayoutSurnameRegisterFragment.error = getString(R.string.register_error_surname_info)
        }

        if(username.length in 4 .. 20 && name.contains(USERNAME_PATTERN)){
            validator = true
            binding.textFieldLayoutUsernameRegisterFragment.error = null
        }else{
            validator = false
            binding.textFieldLayoutUsernameRegisterFragment.error = getString(R.string.register_error_username_info)
        }

        if(password.length in 8 .. 30 && password.contains(PASSWORD_PATTERN)){
            validator = true
            binding.textFieldLayoutPasswordRegisterFragment.error = null
            binding.textFieldLayoutPasswordRepeatRegisterFragment.error = null
        }else if(password != binding.textFieldPasswordRepeatRegisterFragment.text.toString()){
            validator = false
            binding.textFieldLayoutPasswordRegisterFragment.error = getString(R.string.register_error_passwordReapeat_info)
            binding.textFieldLayoutPasswordRepeatRegisterFragment.error = getString(R.string.register_error_passwordReapeat_info)
        }else {
            validator = false
            binding.textFieldLayoutPasswordRegisterFragment.error = getString(R.string.register_error_password_info)
            binding.textFieldLayoutPasswordRepeatRegisterFragment.error = getString(R.string.register_error_password_info)
        }

        return validator
    }

}