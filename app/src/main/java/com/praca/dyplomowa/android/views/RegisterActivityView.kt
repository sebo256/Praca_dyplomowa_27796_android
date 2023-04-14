package com.praca.dyplomowa.android.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.ActivityRegisterViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.viewmodels.RegisterViewModel

class RegisterActivityView : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterViewBinding
    lateinit var viewModelRegister: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelRegister = ViewModelProvider(this).get(RegisterViewModel::class.java)
        setObserverForError()
        binding.buttonRegisterRegisterActivity.setOnClickListener { registerUser() }

        binding.buttonBackRegisterActivity.setOnClickListener { finish() }
    }

    private fun setObserverForError() {
        viewModelRegister.errorResult.observe(this){
            if(it == true) {
                ErrorDialogHandler(this)
                viewModelRegister.errorResult.value = false
            }
        }
    }

    private fun registerUser(){
        val username = binding.textFieldUsernameRegisterActivity.text.toString()
        val password = binding.textFieldPasswordRegisterActivity.text.toString()
        val name = binding.textFieldNameRegisterActivity.text.toString()
        val surname = binding.textFieldSurnameRegisterActivity.text.toString()
        if(validateRegistrationData(username, password, name, surname)){
            viewModelRegister.registerResult.observe(this) {
                if(it.account == null){
                    binding.textFieldLayoutUsernameRegisterActivity.error = getString(R.string.register_error_usernameExists_info)
                }else{
                    finish()
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
            binding.textFieldLayoutNameRegisterActivity.error = null
        }else{
            validator = false
            binding.textFieldLayoutNameRegisterActivity.error = getString(R.string.register_error_name_info)
        }

        if(surname.length in 4 .. 25 && surname.contains(NAME_PATTERN)){
            validator = true
            binding.textFieldLayoutSurnameRegisterActivity.error = null
        }else{
            validator = false
            binding.textFieldLayoutSurnameRegisterActivity.error = getString(R.string.register_error_surname_info)
        }

        if(username.length in 4 .. 20 && name.contains(USERNAME_PATTERN)){
            validator = true
            binding.textFieldLayoutUsernameRegisterActivity.error = null
        }else{
            validator = false
            binding.textFieldLayoutUsernameRegisterActivity.error = getString(R.string.register_error_username_info)
        }

        if(password.length in 8 .. 30 && password.contains(PASSWORD_PATTERN)){
            validator = true
            binding.textFieldLayoutPasswordRegisterActivity.error = null
            binding.textFieldLayoutPasswordRepeatRegisterActivity.error = null
        }else if(password != binding.textFieldPasswordRepeatRegisterActivity.text.toString()){
            validator = false
            binding.textFieldLayoutPasswordRegisterActivity.error = getString(R.string.register_error_passwordReapeat_info)
            binding.textFieldLayoutPasswordRepeatRegisterActivity.error = getString(R.string.register_error_passwordReapeat_info)
        }else {
            validator = false
            binding.textFieldLayoutPasswordRegisterActivity.error = getString(R.string.register_error_password_info)
            binding.textFieldLayoutPasswordRepeatRegisterActivity.error = getString(R.string.register_error_password_info)
        }

        return validator
    }

}