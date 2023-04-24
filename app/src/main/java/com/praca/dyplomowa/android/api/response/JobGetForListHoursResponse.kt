package com.praca.dyplomowa.android.api.response

data class JobGetForListHoursResponse(
    val id: String,
    val subject: String,
    val jobType: String,
    val companyName: String?,
    val name: String,
    val surname: String,
    val street: String,
    val city: String,
    val isCompleted: Boolean,
    val timeSpent: Int
)

data class JobGetForListHoursResponseCollection(
    val collection: Collection<JobGetForListHoursResponse>
)