package com.praca.dyplomowa.android.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        if(validateRegistrationData()){
            viewModelRegister.registerResult.observe(viewLifecycleOwner) {
                if(it.account == null){
                    binding.textFieldLayoutUsernameRegisterFragment.error = getString(R.string.register_error_usernameExists_info)
                }else{
                    parentFragmentManager.popBackStack()
                }
            }
            viewModelRegister.registerUser(
                username = binding.textFieldUsernameRegisterFragment.text.toString(),
                password = binding.textFieldPasswordRegisterFragment.text.toString(),
                name = binding.textFieldNameRegisterFragment.text.toString(),
                surname= binding.textFieldSurnameRegisterFragment.text.toString())
        }
    }

    private fun validateName(name: String): Boolean {
        val NAME_PATTERN = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}".toRegex()
        return if(name.length in 2 .. 25 && name.matches(NAME_PATTERN)){
            binding.textFieldLayoutNameRegisterFragment.error = null
            true
        }else{
            binding.textFieldLayoutNameRegisterFragment.error = getString(R.string.register_error_name_info)
            false
        }
    }

    private fun validateSurname(surname: String): Boolean {
        val NAME_PATTERN = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}['-]{0,2}[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,25}".toRegex()
        return if(surname.length in 2 .. 25 && surname.matches(NAME_PATTERN)){
            binding.textFieldLayoutSurnameRegisterFragment.error = null
            true
        }else{
            binding.textFieldLayoutSurnameRegisterFragment.error = getString(R.string.register_error_surname_info)
            false
        }
    }

    private fun validateUsername(username: String): Boolean {
        val USERNAME_PATTERN = "^[a-zA-Z0-9._-]{4,}\$".toRegex()
        return if(username.length in 4 .. 20 && username.matches(USERNAME_PATTERN)){
            binding.textFieldLayoutUsernameRegisterFragment.error = null
            true
        }else{
            binding.textFieldLayoutUsernameRegisterFragment.error = getString(R.string.register_error_username_info)
            false
        }
    }

    private fun validatePassword(password: String): Boolean {
        val PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%]).{8,30}\$".toRegex()
        return if(password.length in 8 .. 30 && password.matches(PASSWORD_PATTERN)){
            binding.textFieldLayoutPasswordRegisterFragment.error = null
            binding.textFieldLayoutPasswordRepeatRegisterFragment.error = null
            true
        }else if(password != binding.textFieldPasswordRepeatRegisterFragment.text.toString()){
            binding.textFieldLayoutPasswordRegisterFragment.error = getString(R.string.register_error_passwordReapeat_info)
            binding.textFieldLayoutPasswordRepeatRegisterFragment.error = getString(R.string.register_error_passwordReapeat_info)
            false
        }else {
            binding.textFieldLayoutPasswordRegisterFragment.error = getString(R.string.register_error_password_info)
            binding.textFieldLayoutPasswordRepeatRegisterFragment.error = getString(R.string.register_error_password_info)
            false
        }
    }

    private fun validateRegistrationData(): Boolean = listOf(
        validateName(binding.textFieldNameRegisterFragment.text.toString()),
        validateSurname(binding.textFieldSurnameRegisterFragment.text.toString()),
        validateUsername(binding.textFieldUsernameRegisterFragment.text.toString()),
        validatePassword(binding.textFieldPasswordRegisterFragment.text.toString())
    ).all { it }
}