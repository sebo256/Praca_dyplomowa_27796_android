package com.praca.dyplomowa.android.api.repository

import android.content.Context
import com.praca.dyplomowa.android.api.PDyplomowaAPI
import com.praca.dyplomowa.android.api.request.JobApplyToRequest
import com.praca.dyplomowa.android.api.request.JobRequest
import com.praca.dyplomowa.android.api.request.JobRequestUpdate
import com.praca.dyplomowa.android.api.response.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class JobRepository(val context: Context) {

    fun getJobs(): Single<Response<JobGetAllResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobs()

    fun getJobsForList(): Single<Response<JobGetForListResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobsForList()

    fun getJobDatesAndInfo(): Single<Response<JobGetDatesAndInfoResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobDatesAndInfo()

    fun getJobById(objectId: String): Single<Response<JobGetAllResponse>> =
        PDyplomowaAPI.getApi(context).getJobById(objectId = objectId)

    fun getJobAplliedTo(objectId: String): Single<Response<JobAppliedToResponse>> =
        PDyplomowaAPI.getApi(context).getJobAplliedTo(objectId = objectId)

    fun getJobsAppliedToUserAndCheckCompleted(username: String, isCompleted: Boolean): Single<Response<JobGetForListResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobsAppliedToUserAndCheckCompleted(username = username, isCompleted = isCompleted)

    fun countJobsAppliedToUserAndCheckCompleted(username: String, isCompleted: Boolean): Single<Response<Long>> =
        PDyplomowaAPI.getApi(context).countJobsAppliedToUserAndCheckCompleted(username = username, isCompleted = isCompleted)

    fun addJob(jobRequest: JobRequest): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).addJob(jobRequest = jobRequest)

    fun addJobApplyTo(jobApplyToRequest: JobApplyToRequest): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).addJobApplyTo(jobApplyToRequest = jobApplyToRequest)

    fun getJobByLongDateBetween(startLong: Long, endLong: Long): Single<JobGetForListResponseCollection> =
        PDyplomowaAPI.getApi(context).getJobByLongDateBetween(startLong = startLong, endLong = endLong)

    fun getSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompleted(startLong: Long, endLong: Long, username: String, isCompleted: Boolean): Single<Response<Int>> =
        PDyplomowaAPI.getApi(context).getSumOfTimeSpentForSpecifiedMonthAndUserAndCheckCompleted(startLong = startLong, endLong = endLong, username = username, isCompleted = isCompleted)

    fun getAllTimeSpentForUserPerMonth(username: String): Single<Response<JobTimeSpentResponseCollection>> =
        PDyplomowaAPI.getApi(context).getAllTimeSpentForUserPerMonth(username = username)

    fun updateJob(jobRequestUpdate: JobRequestUpdate): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).updateJob(jobRequestUpdate = jobRequestUpdate)

    fun deleteJob(objectId: String): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).deleteJob(objectId = objectId)


}