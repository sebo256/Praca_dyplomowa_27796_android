package com.praca.dyplomowa.android.api

import android.content.Context
import com.praca.dyplomowa.android.api.request.LoginRequest
import com.praca.dyplomowa.android.api.request.RegistrationRequest
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import com.praca.dyplomowa.android.api.response.LoginResponse
import com.praca.dyplomowa.android.api.response.RegistrationResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PDyplomowaAPI {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Single<Response<LoginResponse>>

    @POST("/auth/logout")
    fun logout(@Header("Set-Cookie")token: String): Single<String>

    @POST("auth/register")
    fun register(@Body registrationRequest: RegistrationRequest): Single<Response<RegistrationResponse>>

    @GET("job")
    fun getJobs(): Single<Response<JobGetAllResponseCollection>>

    companion object {
        fun getApi(context: Context): PDyplomowaAPI{
            return ApiClient.getInstance(context).create(PDyplomowaAPI::class.java)
        }
    }

}