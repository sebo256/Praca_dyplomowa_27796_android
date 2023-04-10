package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.response.JobPlannedDateResponse
import com.praca.dyplomowa.android.api.response.JobTimeSpentResponseCollection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class ProfileTimeSpentListViewModel(application: Application): AndroidViewModel(application) {

    val jobRepository = JobRepository(application.baseContext)
    val jobTimeSpentResult: MutableLiveData<JobTimeSpentResponseCollection> = MutableLiveData()

    fun getAllTimeSpentForUserPerMonth(username: String){
        jobRepository.getAllTimeSpentForUserPerMonth(username = username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(2)
            .subscribe(getAllTimeSpentForUserPerMonthObserverRx())
    }

    private fun getAllTimeSpentForUserPerMonthObserverRx(): SingleObserver<Response<JobTimeSpentResponseCollection>> {
        return object : SingleObserver<Response<JobTimeSpentResponseCollection>> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<JobTimeSpentResponseCollection>) {
                jobTimeSpentResult.postValue(t.body())
            }
        }
    }

}