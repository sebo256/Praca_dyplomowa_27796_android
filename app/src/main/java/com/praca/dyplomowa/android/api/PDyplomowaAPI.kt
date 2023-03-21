package com.praca.dyplomowa.android.api

import android.content.Context
import com.praca.dyplomowa.android.api.request.*
import com.praca.dyplomowa.android.api.response.*
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

    @POST("job")
    fun addJob(@Body jobRequest: JobRequest): Single<Response<JobResponse>>

    @POST("job/addJobApplyTo")
    fun addJobApplyTo(@Body jobApplyToRequest: JobApplyToRequest): Single<Response<JobResponse>>

    @GET("job")
    fun getJobs(): Single<Response<JobGetAllResponseCollection>>

    @GET("job/getById")
    fun getJobById(@Body jobGetByIdRequest: JobGetByIdRequest): Single<Response<JobGetAllResponse>>

    @GET("user")
    fun getUsers(): Single<Response<UserGetAllResponseCollection>>

    companion object {
        fun getApi(context: Context): PDyplomowaAPI{
            return ApiClient.getInstance(context).create(PDyplomowaAPI::class.java)
        }
    }

}