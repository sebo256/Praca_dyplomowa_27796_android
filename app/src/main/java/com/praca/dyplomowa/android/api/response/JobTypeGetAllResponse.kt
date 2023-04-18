package com.praca.dyplomowa.android.api.response

data class JobTypeGetAllResponse(
    val id: String,
    val jobType: String
)

data class JobTypeGetAllResponseCollection(
    val collection: Collection<JobTypeGetAllResponse>
)
