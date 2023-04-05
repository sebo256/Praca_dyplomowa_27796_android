package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.response.JobGetForListResponseCollection
import com.praca.dyplomowa.android.api.response.UserGetAllResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    val jobRepository = JobRepository(application.baseContext)
    val userRepository = UserRepository(application.baseContext)
    val jobCompletedCountResult: MutableLiveData<Long> = MutableLiveData()
    val jobTodoCountResult: MutableLiveData<Long> = MutableLiveData()
    val userResponse: MutableLiveData<UserGetAllResponse> = MutableLiveData()

    fun countCompletedJobsAppliedToUserAndCheckCompleted(username: String){
        jobRepository.countJobsAppliedToUserAndCheckCompleted(username = username, isCompleted = true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCountCompletedJobsAppliedToUserAndCheckCompletedObserverRx())
    }

    fun countTodoJobsAppliedToUserAndCheckCompleted(username: String){
        jobRepository.countJobsAppliedToUserAndCheckCompleted(username = username, isCompleted = false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getCountTodoJobsAppliedToUserAndCheckCompletedObserverRx())
    }

    fun getUser(username: String){
        userRepository.getUser(username = username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getUserObserverRx())
    }

    private fun getCountCompletedJobsAppliedToUserAndCheckCompletedObserverRx(): SingleObserver<Response<Long>> {
        return object : SingleObserver<Response<Long>> {
            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<Long>) {
                jobCompletedCountResult.postValue(t.body())
            }





        }
    }

    private fun getCountTodoJobsAppliedToUserAndCheckCompletedObserverRx(): SingleObserver<Response<Long>> {
        return object : SingleObserver<Response<Long>> {
            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<Long>) {
                jobTodoCountResult.postValue(t.body())
            }

        }
    }

    private fun getUserObserverRx(): SingleObserver<Response<UserGetAllResponse>> {
        return object : SingleObserver<Response<UserGetAllResponse>> {
            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<UserGetAllResponse>) {
                userResponse.postValue(t.body())
            }
        }
    }

}