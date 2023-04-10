package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.response.UserGetAllResponse
import com.praca.dyplomowa.android.utils.DateRange
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import java.time.LocalDate
import java.time.ZoneId

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    val jobRepository = JobRepository(application.baseContext)
    val userRepository = UserRepository(application.baseContext)
    val jobCompletedCountResult: MutableLiveData<Long> = MutableLiveData()
    val jobTodoCountResult: MutableLiveData<Long> = MutableLiveData()
    val jobTimeSpentResult: MutableLiveData<Int> = MutableLiveData()
    val userResponse: MutableLiveData<UserGetAllResponse> = MutableLiveData()

    fun countCompletedJobsAppliedToUser(username: String){
        jobRepository.countJobsAppliedToUserAndCheckCompleted(username = username, isCompleted = true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(2)
            .subscribe(getCountCompletedJobsAppliedToUserObserverRx())
    }

    fun countTodoJobsAppliedToUser(username: String){
        jobRepository.countJobsAppliedToUserAndCheckCompleted(username = username, isCompleted = false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(2)
            .subscribe(getCountTodoJobsAppliedToUserObserverRx())
    }

    fun getUser(username: String){
        userRepository.getUser(username = username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(2)
            .subscribe(getUserObserverRx())
    }

    fun getSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompleted(startLong: Long, endLong: Long, username: String){
        jobRepository.getSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompleted(startLong = startLong, endLong = endLong, username = username, isCompleted = true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(2)
            .subscribe(getSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompletedObserverRx())
    }

    private fun getCountCompletedJobsAppliedToUserObserverRx(): SingleObserver<Response<Long>> {
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

    private fun getCountTodoJobsAppliedToUserObserverRx(): SingleObserver<Response<Long>> {
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

    private  fun getSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompletedObserverRx(): SingleObserver<Response<Int>> {
        return object : SingleObserver<Response<Int>> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<Int>) {
                jobTimeSpentResult.postValue(t.body())
            }
        }
    }

    fun getCurrentMonthBeginLong(): Long =
        LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    fun getCurrentMonthEndLong(): Long =
        LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()



}