package com.praca.dyplomowa.android.api

import android.content.Context
import com.praca.dyplomowa.android.api.request.*
import com.praca.dyplomowa.android.api.response.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.*

interface PDyplomowaAPI {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Single<Response<LoginResponse>>

    @POST("/auth/logout")
    fun logout(@Header("Set-Cookie")token: String): Single<String>

    @POST("auth/register")
    fun register(@Body registrationRequest: RegistrationRequest): Single<Response<RegistrationResponse>>

    @POST("job")
    fun addJob(@Body jobRequest: JobRequest): Single<Response<JobResponse>>

    @GET("job")
    fun getJobs(): Single<Response<JobGetAllResponseCollection>>

    @GET("job/getById/{objectId}")
    fun getJobById(@Path("objectId") objectId: String): Single<Response<JobGetAllResponse>>

    @GET("job/getById/jobAppliedTo/{objectId}")
    fun getJobAplliedTo(@Path("objectId") objectId: String): Single<Response<JobAppliedToResponse>>

    @GET("user")
    fun getUsers(): Single<Response<UserGetAllResponseCollection>>

    @PUT("job/addJobApplyTo")
    fun addJobApplyTo(@Body jobApplyToRequest: JobApplyToRequest): Single<Response<JobResponse>>

    @PUT("job")
    fun updateJob(@Body jobRequestUpdate: JobRequestUpdate): Single<Response<JobResponse>>

    companion object {
        fun getApi(context: Context): PDyplomowaAPI{
            return ApiClient.getInstance(context).create(PDyplomowaAPI::class.java)
        }
    }

}