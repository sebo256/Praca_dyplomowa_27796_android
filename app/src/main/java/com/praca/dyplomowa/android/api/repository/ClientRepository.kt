package com.praca.dyplomowa.android.api.repository

import android.content.Context
import com.praca.dyplomowa.android.api.PDyplomowaAPI
import com.praca.dyplomowa.android.api.request.ClientRequest
import com.praca.dyplomowa.android.api.request.ClientRequestUpdate
import com.praca.dyplomowa.android.api.response.ClientGetAllResponse
import com.praca.dyplomowa.android.api.response.ClientGetAllResponseCollection
import com.praca.dyplomowa.android.api.response.ClientResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class ClientRepository(val context: Context) {

    fun addClient(clientRequest: ClientRequest): Single<Response<ClientResponse>> =
        PDyplomowaAPI.getApi(context).addClient(clientRequest)

    fun getClients(): Single<Response<ClientGetAllResponseCollection>> =
        PDyplomowaAPI.getApi(context).getClients()

    fun getClientById(objectId: String): Single<Response<ClientGetAllResponse>> =
        PDyplomowaAPI.getApi(context).getClientById(objectId)

    fun updateClient(clientRequestUpdate: ClientRequestUpdate): Single<Response<ClientResponse>> =
        PDyplomowaAPI.getApi(context).updateClient(clientRequestUpdate)

    fun deleteClient(objectId: String): Single<Response<ClientResponse>> =
        PDyplomowaAPI.getApi(context).deleteClient(objectId)

}