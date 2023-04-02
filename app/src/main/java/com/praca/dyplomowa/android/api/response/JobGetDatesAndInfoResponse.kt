package com.praca.dyplomowa.android.api.response

data class JobGetDatesAndInfoResponse(
    val id: String,
    val subject: String,
    val plannedDate: Long?,
    val isCompleted: Boolean
)

data class JobGetDatesAndInfoResponseCollection(
    val collection: Collection<JobGetDatesAndInfoResponse>
)
