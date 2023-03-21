package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.JobRepository
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.request.JobRequest
import com.praca.dyplomowa.android.api.response.JobResponse
import com.praca.dyplomowa.android.api.response.UserGetAllResponse
import com.praca.dyplomowa.android.api.response.UserGetAllResponseCollection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class AddJobsViewModel(application: Application): AndroidViewModel(application) {

    val jobRepository = JobRepository(application.baseContext)
    val jobResult: MutableLiveData<JobResponse> = MutableLiveData()

    fun addJob(jobRequest: JobRequest){
        jobRepository.addJob(jobRequest = jobRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getAddedJobListObserverRx())
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


    fun calculatePlannedDate(date: Long, hour: Int, minutes: Int): Long =
        (date) + ((hour * 3600000) + (minutes * 60000))


    //TODO Na później do pobierania userów
//    val userRepository = UserRepository(application.baseContext)
//    val userResult: MutableLiveData<UserGetAllResponseCollection> = MutableLiveData()
//
//    fun getUsers(){
//        userRepository.getUsers()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(getAllUsersListObserverRx())
//    }
//
//    private fun getAllUsersListObserverRx(): SingleObserver<Response<UserGetAllResponseCollection>>{
//        return object : SingleObserver<Response<UserGetAllResponseCollection>> {
//
//            override fun onError(e: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                //Loading
//            }
//
//            override fun onSuccess(t: Response<UserGetAllResponseCollection>) {
//                userResult.postValue(t.body())
//            }
//
//        }
//    }
}