package com.praca.dyplomowa.android.api.repository

import android.content.Context
import com.praca.dyplomowa.android.api.PDyplomowaAPI
import com.praca.dyplomowa.android.api.request.LoginRequest
import com.praca.dyplomowa.android.api.request.RegistrationRequest
import com.praca.dyplomowa.android.api.response.LoginResponse
import com.praca.dyplomowa.android.api.response.RegistrationResponse
import com.praca.dyplomowa.android.api.response.UserGetAllResponse
import com.praca.dyplomowa.android.api.response.UserGetAllResponseCollection
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class UserRepository(val context: Context) {

    fun login(loginRequest: LoginRequest): Single<Response<LoginResponse>> =
        PDyplomowaAPI.getApi(context).login(loginRequest = loginRequest)

    fun register(registrationRequest: RegistrationRequest): Single<Response<RegistrationResponse>> =
        PDyplomowaAPI.getApi(context).register(registrationRequest = registrationRequest)

    fun getUsers(): Single<Response<UserGetAllResponseCollection>> =
        PDyplomowaAPI.getApi(context).getUsers()

}