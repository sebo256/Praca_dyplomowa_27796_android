package com.praca.dyplomowa.android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.praca.dyplomowa.android.api.repository.ClientRepository
import com.praca.dyplomowa.android.api.request.ClientRequest
import com.praca.dyplomowa.android.api.request.ClientRequestUpdate
import com.praca.dyplomowa.android.api.response.ClientGetAllResponse
import com.praca.dyplomowa.android.api.response.ClientResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class JobClientAddViewModel(application: Application): AndroidViewModel(application) {

    val clientRepository = ClientRepository(application.baseContext)
    val clientAddOrUpdateResult: MutableLiveData<ClientResponse> = MutableLiveData()
    val clientResult: MutableLiveData<ClientGetAllResponse> = MutableLiveData()
    val errorResult: MutableLiveData<Boolean> = MutableLiveData()

    fun addClient(clientRequest: ClientRequest){
        clientRepository.addClient(clientRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(addClientObserverRx())
    }

    fun getClientById(objectId: String){
        clientRepository.getClientById(objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getClientByIdObserverRx())
    }

    fun updateClient(clientRequestUpdate: ClientRequestUpdate){
        clientRepository.updateClient(clientRequestUpdate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(updateClientObserverRx())
    }

    private fun addClientObserverRx(): SingleObserver<Response<ClientResponse>> {
        return object : SingleObserver<Response<ClientResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<ClientResponse>) {
                clientAddOrUpdateResult.postValue(t.body())
            }
        }
    }

    private fun getClientByIdObserverRx(): SingleObserver<Response<ClientGetAllResponse>> {
        return object : SingleObserver<Response<ClientGetAllResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<ClientGetAllResponse>) {
                clientResult.postValue(t.body())
            }
        }
    }

    private fun updateClientObserverRx(): SingleObserver<Response<ClientResponse>> {
        return object : SingleObserver<Response<ClientResponse>> {

            override fun onError(e: Throwable) {
                errorResult.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: Response<ClientResponse>) {
                clientAddOrUpdateResult.postValue(t.body())
            }
        }
    }

}