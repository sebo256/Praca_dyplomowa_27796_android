package com.praca.dyplomowa.android.api.response

data class JobGetForListResponse(
    val id: String,
    val subject: String,
    val companyName: String?,
    val name: String,
    val surname: String,
    val street: String,
    val city: String,
    val isCompleted: Boolean
)

data class JobGetForListResponseCollection(
    val collection: Collection<JobGetForListResponse>
)

