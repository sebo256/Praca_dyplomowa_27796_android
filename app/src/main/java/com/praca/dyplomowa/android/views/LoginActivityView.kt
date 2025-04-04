package com.praca.dyplomowa.android.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.praca.dyplomowa.android.R
import com.praca.dyplomowa.android.databinding.ActivityLoginViewBinding
import com.praca.dyplomowa.android.utils.ErrorDialogHandler
import com.praca.dyplomowa.android.utils.FragmentNavigationUtils
import com.praca.dyplomowa.android.utils.SessionManager
import com.praca.dyplomowa.android.viewmodels.LoginViewModel


class LoginActivityView : AppCompatActivity() {
    private lateinit var binding: ActivityLoginViewBinding
    lateinit var viewModelLogin: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelLogin = ViewModelProvider(this).get(LoginViewModel::class.java)
        setObserverForError()
        val token = SessionManager.getAccessToken(this)
        if(!token.isNullOrBlank()){
            refreshAccessToken()
        }

        binding.buttonLoginLoginActivity.setOnClickListener { loginUser() }

        binding.buttonRegisterLoginActivity.setOnClickListener {
            FragmentNavigationUtils.addFragmentFade(
                fragmentManager = supportFragmentManager,
                fragment = RegisterFragmentView()
            )
        }

    }

    private fun refreshAccessToken(){
        viewModelLogin.refreshToken(SessionManager.getRefreshToken(this)!!)
        viewModelLogin.refreshTokenResultBool.observe(this) {
            if(it){
                goToHome()
            }
        }
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivityView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun loginUser() {
        val username = binding.textFieldUsernameLoginActivity.text.toString()
        val password = binding.textFieldPasswordLoginActivity.text.toString()
        if (username.length in 3..19 && password.length in 8..30) {
            viewModelLogin.loginResult.observe(this) {
                if (it != null) {
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

    private fun setObserverForError() {
        viewModelLogin.errorResult.observe(this){
            if(it == true) {
                ErrorDialogHandler(this)
                viewModelLogin.errorResult.value = false
            }
        }
    }

}