package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.request.RegistrationRequest
import com.praca.dyplomowa.android.api.response.RegistrationResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class RegisterViewModel(application: Application): AndroidViewModel(application) {

    val userRepository = UserRepository(application.baseContext)
    val registerResult: MutableLiveData<RegistrationResponse> = MutableLiveData()
    val errorResult: MutableLiveData<Boolean> = MutableLiveData()

    fun registerUser(username: String, password: String, name: String, surname: String){
        userRepository.register(RegistrationRequest(
            username = username,
            password = password,
            name = name,
            surname = surname
          )).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(getRegisterListObserverRx())

    }

    private fun getRegisterListObserverRx(): SingleObserver<Response<RegistrationResponse>>{
        return object : SingleObserver<Response<RegistrationResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<RegistrationResponse>) {
                registerResult.postValue(t.body())
            }

        }

    }

}