package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import com.praca.dyplomowa.android.api.response.JobGetDatesAndInfoResponseCollection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class CalendarViewModel(application: Application): AndroidViewModel(application) {

    val jobRepository = JobRepository(application.baseContext)
    val jobDatesAndInfoResult: MutableLiveData<JobGetDatesAndInfoResponseCollection> = MutableLiveData()
    val jobResult: MutableLiveData<JobGetAllResponseCollection> = MutableLiveData()

    fun getJobDatesAndInfo(){
        jobRepository.getJobDatesAndInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getJobDatesAndInfoListObserverRx())
    }

    fun getJobByLongDateBetween(startLong: Long, endLong: Long){
        jobRepository.getJobByLongDateBetween(startLong = startLong, endLong = endLong)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getJobByLongDateBetweenListObserverRx())
    }

    fun getJobDatesAndInfoListObserverRx(): SingleObserver<Response<JobGetDatesAndInfoResponseCollection>> {
        return object : SingleObserver<Response<JobGetDatesAndInfoResponseCollection>> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<JobGetDatesAndInfoResponseCollection>) {
                jobDatesAndInfoResult.postValue(t.body())
            }
        }
    }

    fun getJobByLongDateBetweenListObserverRx(): SingleObserver<JobGetAllResponseCollection>{
        return object : SingleObserver<JobGetAllResponseCollection> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: JobGetAllResponseCollection) {
                jobResult.postValue(t)
            }
        }
    }

}