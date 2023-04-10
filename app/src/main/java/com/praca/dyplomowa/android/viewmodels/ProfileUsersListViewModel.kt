package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.response.UserGetAllResponseCollection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class ProfileUsersListViewModel(application: Application): AndroidViewModel(application) {

    val userRepository = UserRepository(application.baseContext)
    val userReponse: MutableLiveData<UserGetAllResponseCollection> = MutableLiveData()

    fun getUsers(){
        userRepository.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(2)
            .subscribe(getUsersObserverRx())
    }

    private fun getUsersObserverRx(): SingleObserver<Response<UserGetAllResponseCollection>> {
        return object : SingleObserver<Response<UserGetAllResponseCollection>> {
            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<UserGetAllResponseCollection>) {
                userReponse.postValue(t.body())
            }
        }
    }

}