package com.praca.dyplomowa.android.api.repository

import android.content.Context
import com.praca.dyplomowa.android.api.PDyplomowaAPI
import com.praca.dyplomowa.android.api.request.*
import com.praca.dyplomowa.android.api.response.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Query

class JobRepository(val context: Context) {

    fun getJobs(): Single<Response<JobGetAllResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobs()

    fun getJobDatesAndInfo(): Single<Response<JobGetDatesAndInfoResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobDatesAndInfo()

    fun getJobById(objectId: String): Single<Response<JobGetAllResponse>> =
        PDyplomowaAPI.getApi(context).getJobById(objectId = objectId)

    fun getJobAplliedTo(objectId: String): Single<Response<JobAppliedToResponse>> =
        PDyplomowaAPI.getApi(context).getJobAplliedTo(objectId = objectId)

    fun addJob(jobRequest: JobRequest): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).addJob(jobRequest = jobRequest)

    fun addJobApplyTo(jobApplyToRequest: JobApplyToRequest): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).addJobApplyTo(jobApplyToRequest = jobApplyToRequest)

    fun getJobByLongDateBetween(startLong: Long, endLong: Long): Single<JobGetAllResponseCollection> =
        PDyplomowaAPI.getApi(context).getJobByLongDateBetween(startLong = startLong, endLong = endLong)

    fun updateJob(jobRequestUpdate: JobRequestUpdate): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).updateJob(jobRequestUpdate = jobRequestUpdate)

    fun deleteJob(objectId: String): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).deleteJob(objectId = objectId)


}