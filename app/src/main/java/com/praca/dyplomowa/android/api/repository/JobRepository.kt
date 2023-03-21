package com.praca.dyplomowa.android.api.repository

import android.content.Context
import com.praca.dyplomowa.android.api.PDyplomowaAPI
import com.praca.dyplomowa.android.api.request.JobApplyToRequest
import com.praca.dyplomowa.android.api.request.JobGetByIdRequest
import com.praca.dyplomowa.android.api.request.JobRequest
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import com.praca.dyplomowa.android.api.response.JobResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class JobRepository(val context: Context) {

    fun getJobs(): Single<Response<JobGetAllResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobs()

    fun getJobById(jobGetByIdRequest: JobGetByIdRequest): Single<Response<JobGetAllResponse>> =
        PDyplomowaAPI.getApi(context).getJobById(jobGetByIdRequest)

    fun addJob(jobRequest: JobRequest): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).addJob(jobRequest = jobRequest)

    fun addJobApplyTo(jobApplyToRequest: JobApplyToRequest): Single<Response<JobResponse>> =
        PDyplomowaAPI.getApi(context).addJobApplyTo(jobApplyToRequest = jobApplyToRequest)

}