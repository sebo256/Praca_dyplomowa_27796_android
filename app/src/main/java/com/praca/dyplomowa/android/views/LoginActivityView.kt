package com.praca.dyplomowa.android.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.ActivityLoginViewBinding
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.LoginViewModel

    private lateinit var binding: ActivityLoginViewBinding
    lateinit var viewModelLogin: LoginViewModel

class LoginActivityView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = SessionManager.getAccessToken(this)
        if(!token.isNullOrBlank()){
            goToHome()
        }

        binding.buttonLoginLoginActivity.setOnClickListener { loginUser() }

        binding.buttonRegisterLoginActivity.setOnClickListener { startActivity(Intent(this, RegisterActivityView::class.java)) }

    }

    private fun goToHome() {
        val intent = Intent(this, MainActivityView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun loginUser() {
        val username = binding.textFieldUsernameLoginActivity.text.toString()
        val password = binding.textFieldPasswordLoginActivity.text.toString()
        if (username.length in 3..19 && password.length in 8..30) {
            viewModelLogin = ViewModelProvider(this).get(LoginViewModel::class.java)
            viewModelLogin.loginResult.observe(this) {
                if (it != null) {
                    println(it)
                    goToHome()
                } else {
                    binding.textFieldLayoutUsernameLoginActivity.error =
                        getString(R.string.login_error_info)
                    binding.textFieldLayoutPasswordLoginActivity.error =
                        getString(R.string.login_error_info)
                }
            }
            viewModelLogin.loginUser(username = username, password = password)
        }
    }
}