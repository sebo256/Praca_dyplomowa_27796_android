package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.request.LoginRequest
import com.praca.dyplomowa.android.api.response.LoginResponse
import com.praca.dyplomowa.android.utils.SessionManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import java.util.*

class LoginViewModel(application: Application): AndroidViewModel(application) {

    val userRepository = UserRepository(application.baseContext)
    val loginResult: MutableLiveData<LoginResponse> = MutableLiveData()

    fun loginUser(username: String, password: String) {
        userRepository.login(LoginRequest(username = username, password = password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getLoginListObserverRx())

    }

    private fun getLoginListObserverRx(): SingleObserver<Response<LoginResponse>>{
        return object : SingleObserver<Response<LoginResponse>> {

        override fun onError(e: Throwable) {
            Log.e("e", "Error $e")
        }

        override fun onSubscribe(d: Disposable) {
            //Loading
        }

        override fun onSuccess(t: Response<LoginResponse>) {
            SessionManager.saveAccessToken(
                context = getApplication<Application>().applicationContext,
                token = t.body()?.jwt
            )
            SessionManager.saveRefreshToken(
                context = getApplication<Application>().applicationContext,
                token = t.headers().get("Set-Cookie")
            )
            SessionManager.saveCurrentUserUsername(
                context = getApplication<Application>().applicationContext,
                id = t.body()?.username
            )
            loginResult.postValue(t.body())
        }


        }
    }
}