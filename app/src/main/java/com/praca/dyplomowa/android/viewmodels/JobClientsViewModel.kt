package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.ClientRepository
import com.praca.dyplomowa.android.api.response.ClientGetAllResponseCollection
import com.praca.dyplomowa.android.api.response.ClientResponse
import com.praca.dyplomowa.android.api.response.ObjectId
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class JobClientsViewModel(application: Application): AndroidViewModel(application) {

    val clientRepository = ClientRepository(application.baseContext)
    val clientResult: MutableLiveData<ClientGetAllResponseCollection> = MutableLiveData()
    val deleteResult: MutableLiveData<ClientResponse> = MutableLiveData()
    val errorResult: MutableLiveData<Boolean> = MutableLiveData()

    fun getClients(){
        clientRepository.getClients()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(getClientsObserverRx())
    }

    fun deleteClients(objectId: String){
        clientRepository.deleteClient(objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(1)
            .subscribe(deleteClientObserverRx())
    }

    private fun getClientsObserverRx(): SingleObserver<Response<ClientGetAllResponseCollection>> {
        return object : SingleObserver<Response<ClientGetAllResponseCollection>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<ClientGetAllResponseCollection>) {
                clientResult.postValue(t.body())
            }
        }
    }

    private fun deleteClientObserverRx(): SingleObserver<Response<ClientResponse>> {
        return object : SingleObserver<Response<ClientResponse>> {
            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<ClientResponse>) {
                when(t.code() == 200){
                    true -> deleteResult.postValue(t.body())
                    false -> getClients()
                }

            }
        }
    }

}