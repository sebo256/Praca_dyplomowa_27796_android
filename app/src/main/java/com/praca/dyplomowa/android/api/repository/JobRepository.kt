package com.praca.dyplomowa.android.api.repository

import android.content.Context
import com.praca.dyplomowa.android.api.PDyplomowaAPI
import com.praca.dyplomowa.android.api.response.JobGetAllResponse
import com.praca.dyplomowa.android.api.response.JobGetAllResponseCollection
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class JobRepository(val context: Context) {

    fun getJobs(): Single<Response<JobGetAllResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobs()

}