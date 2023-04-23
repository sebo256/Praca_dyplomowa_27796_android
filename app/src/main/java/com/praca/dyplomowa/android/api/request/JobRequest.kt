package com.praca.dyplomowa.android.api.request

data class JobRequest(
    val client: String,
    val subject: String,
    val jobType: String,
    val plannedDate: Long?,
    val note: String?,
    val isCompleted: Boolean,
    val createdBy: String
)
