package com.praca.dyplomowa.android.api.repository

import android.content.Context
import com.praca.dyplomowa.android.api.PDyplomowaAPI
import com.praca.dyplomowa.android.api.response.JobTypeGetAllResponseCollection
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class JobTypeRepository(val context: Context) {

    fun getJobTypes(): Single<Response<JobTypeGetAllResponseCollection>> =
        PDyplomowaAPI.getApi(context).getJobTypes()

}