package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import com.praca.dyplomowa.android.api.response.JobGetForListResponseCollection
import com.praca.dyplomowa.android.api.response.JobResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class JobsViewModel(application: Application): AndroidViewModel(application) {

    val jobRepository = JobRepository(application.baseContext)
    val jobResult: MutableLiveData<JobGetForListResponseCollection> = MutableLiveData()
    val jobDeleteResult: MutableLiveData<JobResponse> = MutableLiveData()
    val errorResult: MutableLiveData<Boolean> = MutableLiveData()

    fun getJobs(){
        jobRepository.getJobsForList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(getAllJobsListObserverRx())
    }

    fun deleteJob(objectId: String){
        jobRepository.deleteJob(objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(deleteJobObserverRx())
    }

    private fun getAllJobsListObserverRx(): SingleObserver<Response<JobGetForListResponseCollection>>{
        return object : SingleObserver<Response<JobGetForListResponseCollection>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<JobGetForListResponseCollection>) {
                jobResult.postValue(t.body())
            }
        }
    }

    private fun deleteJobObserverRx(): SingleObserver<Response<JobResponse>>{
         return object : SingleObserver<Response<JobResponse>> {

             override fun onError(e: Throwable) {
                 errorResult.postValue(true)
             }

             override fun onSubscribe(d: Disposable) {

             }

             override fun onSuccess(t: Response<JobResponse>) {
                 jobDeleteResult.postValue(t.body())
             }
         }
    }

}