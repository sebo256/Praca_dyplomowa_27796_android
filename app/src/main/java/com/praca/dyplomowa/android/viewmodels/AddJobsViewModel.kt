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

    fun addJob(jobRequest: JobRequest){
        jobRepository.addJob(jobRequest = jobRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getAddedJobListObserverRx())
    }

    fun getJobById(objectId: String){
        jobRepository.getJobById(objectId = objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getJobByIdObserverRx())
    }

    fun updateJob(jobRequestUpdate: JobRequestUpdate){
        jobRepository.updateJob(jobRequestUpdate = jobRequestUpdate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(updateJobObserverRx())
    }

    private fun getAddedJobListObserverRx(): SingleObserver<Response<JobResponse>> {
        return object : SingleObserver<Response<JobResponse>> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<JobResponse>) {
                jobResult.postValue(t.body())
            }

        }
    }

    private fun getJobByIdObserverRx(): SingleObserver<Response<JobGetAllResponse>> {
        return object : SingleObserver<Response<JobGetAllResponse>> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<JobGetAllResponse>) {
                jobGetByIdResult.postValue(t.body())
            }
        }
    }

    private fun updateJobObserverRx(): SingleObserver<Response<JobResponse>> {
        return object : SingleObserver<Response<JobResponse>> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<JobResponse>) {
                jobResult.postValue(t.body())
            }
        }
    }


    fun calculatePlannedDate(date: Long, hour: Int, minutes: Int): Long =
        (date) + ((hour * 3600000) + (minutes * 60000))

    fun calculateSimpleDateFromLong(date: Long?): String {
        return if(date!! > 0) {
            SimpleDateFormat("dd.MM.yyyy").format(Date(date))
        }else{
            ""
        }
    }

    fun calculateHourFromLong(date: Long): Int =
        SimpleDateFormat("HH").format(date).toString().toInt()

    fun calculateMinutesFromLong(date: Long): Int =
        SimpleDateFormat("mm").format(date).toString().toInt()


}