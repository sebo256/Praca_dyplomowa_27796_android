package com.praca.dyplomowa.android.api.response

data class JobTimeSpentResponse(
    val name: String,
    val timeSpent: Int,
)

data class JobTimeSpentResponseCollection(
    val collection: Collection<JobTimeSpentResponse>
)
