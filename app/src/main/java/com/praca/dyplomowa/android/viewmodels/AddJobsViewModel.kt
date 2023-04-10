package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.request.JobRequest
import com.praca.dyplomowa.android.api.request.JobRequestUpdate
import com.praca.dyplomowa.android.api.response.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddJobsViewModel(application: Application): AndroidViewModel(application) {

    val jobRepository = JobRepository(application.baseContext)
    val jobResult: MutableLiveData<JobResponse> = MutableLiveData()
    val jobGetByIdResult: MutableLiveData<JobGetAllResponse> = MutableLiveData()
    val errorResult: MutableLiveData<Boolean> = MutableLiveData()

    fun addJob(jobRequest: JobRequest){
        jobRepository.addJob(jobRequest = jobRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(getAddedJobListObserverRx())
    }

    fun getJobById(objectId: String){
        jobRepository.getJobById(objectId = objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(getJobByIdObserverRx())
    }

    fun updateJob(jobRequestUpdate: JobRequestUpdate){
        jobRepository.updateJob(jobRequestUpdate = jobRequestUpdate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(updateJobObserverRx())
    }

    private fun getAddedJobListObserverRx(): SingleObserver<Response<JobResponse>> {
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

    private fun getJobByIdObserverRx(): SingleObserver<Response<JobGetAllResponse>> {
        return object : SingleObserver<Response<JobGetAllResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<JobGetAllResponse>) {
                jobGetByIdResult.postValue(t.body())
            }
        }
    }

    private fun updateJobObserverRx(): SingleObserver<Response<JobResponse>> {
        return object : SingleObserver<Response<JobResponse>> {

            override fun onError(e: Throwable) {

            }

            override fun onSubscribe(d: Disposable) {
                errorResult.postValue(true)
            }

            override fun onSuccess(t: Response<JobResponse>) {
                jobResult.postValue(t.body())
            }
        }
    }

    fun calculateSimpleDateFromLong(date: Long?): String {
        return if(date!! > 0) {
            SimpleDateFormat("dd.MM.yyyy").format(Date(date))
        }else{
            ""
        }
    }
    
}