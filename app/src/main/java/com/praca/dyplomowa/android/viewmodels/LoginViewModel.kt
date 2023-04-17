package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.request.LoginRequest
import com.praca.dyplomowa.android.api.response.LoginResponse
import com.praca.dyplomowa.android.api.response.RefreshTokenResponse
import com.praca.dyplomowa.android.utils.SessionManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class LoginViewModel(application: Application): AndroidViewModel(application) {

    val userRepository = UserRepository(application.baseContext)
    val loginResult: MutableLiveData<LoginResponse> = MutableLiveData()
    val refreshTokenResultBool: MutableLiveData<Boolean> = MutableLiveData()
    val errorResult: MutableLiveData<Boolean> = MutableLiveData()

    fun loginUser(username: String, password: String) {
        userRepository.login(LoginRequest(username = username, password = password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getLoginListObserverRx())
    }

    fun refreshToken(token: String){
        userRepository.refreshToken(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getNewAccessTokenUsingRefreshObserverRX())

    }

    private fun getLoginListObserverRx(): SingleObserver<Response<LoginResponse>>{
        return object : SingleObserver<Response<LoginResponse>> {

        override fun onError(e: Throwable) {
            errorResult.postValue(true)
        }

        override fun onSubscribe(d: Disposable) {

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
            SessionManager.saveIsAdmin(
                context = getApplication<Application>().applicationContext,
                isAdmin = t.body()?.roles?.contains("ROLE_ADMIN") ?: false
            )
            loginResult.postValue(t.body())
        }


        }
    }

    private fun getNewAccessTokenUsingRefreshObserverRX(): SingleObserver<Response<RefreshTokenResponse>> {
        return object : SingleObserver<Response<RefreshTokenResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<RefreshTokenResponse>) {
                if(t.body()!!.status){
                    SessionManager.saveAccessToken(
                        context = getApplication<Application>().applicationContext,
                        token = t.body()?.token
                    )
                    refreshTokenResultBool.value = true
                }else{
                    SessionManager.clearSharedPrefs(getApplication<Application>().applicationContext)
                    refreshTokenResultBool.value = false
                }


            }

        }
    }
}