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

    @POST("/auth/refreshtoken")
    fun refreshToken(@Header("Cookie")token: String): Single<Response<RefreshTokenResponse>>

    @POST("job")
    fun addJob(@Body jobRequest: JobRequest): Single<Response<JobResponse>>

    @POST("client")
    fun addClient(@Body clientRequest: ClientRequest): Single<Response<ClientResponse>>

    @GET("job")
    fun getJobs(): Single<Response<JobGetAllResponseCollection>>

    @GET("job/getJobsForList")
    fun getJobsForList(): Single<Response<JobGetForListResponseCollection>>

    @GET("job/getDatesAndInfo")
    fun getJobDatesAndInfo(): Single<Response<JobGetDatesAndInfoResponseCollection>>

    @GET("job/getById/{objectId}")
    fun getJobById(@Path("objectId") objectId: String): Single<Response<JobGetAllResponse>>

    @GET("job/getById/jobAppliedTo/{objectId}")
    fun getJobAplliedTo(@Path("objectId") objectId: String): Single<Response<JobAppliedToResponse>>

    @GET("job/getByUsername/getJobsAppliedToUserAndCheckCompletion/?")
    fun getJobsAppliedToUserAndCheckCompleted(@Query("username") username: String, @Query("isCompleted") isCompleted: Boolean): Single<Response<JobGetForListResponseCollection>>

    @GET("job/getByUsername/countJobsAppliedToUserAndCheckCompletion/?")
    fun countJobsAppliedToUserAndCheckCompleted(@Query("username") username: String, @Query("isCompleted") isCompleted: Boolean): Single<Response<Long>>

    @GET("job/getByLongDateBetween/?")
    fun getJobByLongDateBetween(@Query("startLong") startLong: Long, @Query("endLong") endLong: Long): Single<JobGetForListResponseCollection>

    @GET("job/getSumOfTimeSpentForSpecifiedMonthAndUser/?")
    fun getSumOfTimeSpentForSpecifiedMonthAndUser(@Query("startLong") startLong: Long, @Query("endLong") endLong: Long, @Query("username") username: String): Single<Response<Int>>

    @GET("job/getJobsForSpecifiedMonthAndUser/?")
    fun getJobsForSpecifiedMonthAndUser(@Query("startLong") startLong: Long, @Query("endLong") endLong: Long, @Query("username") username: String): Single<Response<JobGetForListHoursResponseCollection>>

    @GET("job/getAllTimeSpentForUserPerMonth/?")
    fun getAllTimeSpentForUserPerMonth(@Query("username") username: String): Single<Response<JobTimeSpentResponseCollection>>

    @GET("jobType")
    fun getJobTypes(): Single<Response<JobTypeGetAllResponseCollection>>

    @GET("client")
    fun getClients(): Single<Response<ClientGetAllResponseCollection>>

    @GET("client/getById/{objectId}")
    fun getClientById(@Path("objectId") objectId: String): Single<Response<ClientGetAllResponse>>

    @GET("user")
    fun getUsers(): Single<Response<UserGetAllResponseCollection>>

    @GET("user/{username}")
    fun getUser(@Path("username") username: String): Single<Response<UserGetAllResponse>>

    @PUT("job/addJobApplyTo")
    fun addJobApplyTo(@Body jobApplyToRequest: JobApplyToRequest): Single<Response<JobResponse>>

    @PUT("job/addTime")
    fun addTimeSpent(@Body jobAddTimeSpentRequest: JobAddTimeSpentRequest): Single<Response<JobResponse>>

    @PUT("job")
    fun updateJob(@Body jobRequestUpdate: JobRequestUpdate): Single<Response<JobResponse>>

    @PUT("client")
    fun updateClient(@Body clientRequestUpdate: ClientRequestUpdate): Single<Response<ClientResponse>>

    @DELETE("job/{objectId}")
    fun deleteJob(@Path("objectId") objectId: String): Single<Response<JobResponse>>

    @DELETE("client/{objectId}")
    fun deleteClient(@Path("objectId") objectId: String): Single<Response<ClientResponse>>

    companion object {
        fun getApi(context: Context): PDyplomowaAPI{
            return ApiClient.getInstance(context).create(PDyplomowaAPI::class.java)
        }
    }

}