package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class JobsViewModel(application: Application): AndroidViewModel(application) {

    val jobRepository = JobRepository(application.baseContext)
    val jobResult: MutableLiveData<JobGetAllResponseCollection> = MutableLiveData()

    fun getJobs(){
        jobRepository.getJobs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getAllJobsListObserverRx())
    }

    private fun getAllJobsListObserverRx(): SingleObserver<Response<JobGetAllResponseCollection>>{
        return object : SingleObserver<Response<JobGetAllResponseCollection>> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<JobGetAllResponseCollection>) {
                jobResult.postValue(t.body())
            }
        }
    }

}