package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.request.JobAddTimeSpentRequest
import com.praca.dyplomowa.android.api.request.JobApplyToRequest
import com.praca.dyplomowa.android.api.response.JobAppliedToResponse
import com.praca.dyplomowa.android.api.response.JobResponse
import com.praca.dyplomowa.android.api.response.UserGetAllResponseCollection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class JobApplyToViewModel(application: Application): AndroidViewModel(application) {


    val userRepository = UserRepository(application.baseContext)
    val jobRepository = JobRepository(application.baseContext)
    val userResult: MutableLiveData<UserGetAllResponseCollection> = MutableLiveData()
    val jobAppliedToRequestResult: MutableLiveData<JobAppliedToResponse> = MutableLiveData()
    val jobResult: MutableLiveData<JobResponse> = MutableLiveData()
    val addTimeSpentResult: MutableLiveData<JobResponse> = MutableLiveData()
    val errorResult: MutableLiveData<Boolean> = MutableLiveData()

    fun getUsers(){
        userRepository.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(getAllUsersListObserverRx())
    }

    fun getJobAppliedTo(objectId: String){
        jobRepository.getJobAplliedTo(objectId = objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(getJobAppliedToObserverRx())
    }

    fun addJobApplyTo(objectId: String, jobAppliedTo: Collection<String>){
        jobRepository.addJobApplyTo(JobApplyToRequest(
            objectId = objectId,
            jobAppliedTo = jobAppliedTo
        ))  .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(usersJobApplyToListObserverRx())
    }

    fun addTimeSpent(jobAddTimeSpentRequest: JobAddTimeSpentRequest){
        jobRepository.addTimeSpent(jobAddTimeSpentRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(addTimeSpentObserverRx())
    }

    private fun getAllUsersListObserverRx(): SingleObserver<Response<UserGetAllResponseCollection>> {
        return object : SingleObserver<Response<UserGetAllResponseCollection>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<UserGetAllResponseCollection>) {
                userResult.postValue(t.body())
            }

        }
    }

    private fun getJobAppliedToObserverRx(): SingleObserver<Response<JobAppliedToResponse>> {
        return object : SingleObserver<Response<JobAppliedToResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<JobAppliedToResponse>) {
                jobAppliedToRequestResult.postValue(t.body())
            }
        }
    }


    private fun usersJobApplyToListObserverRx(): SingleObserver<Response<JobResponse>> {
        return object : SingleObserver<Response<JobResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<JobResponse>) {
                jobResult.postValue(t.body())
            }
        }
    }

    private fun addTimeSpentObserverRx(): SingleObserver<Response<JobResponse>> {
        return object : SingleObserver<Response<JobResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<JobResponse>) {
                addTimeSpentResult.postValue(t.body())
            }
        }
    }

}