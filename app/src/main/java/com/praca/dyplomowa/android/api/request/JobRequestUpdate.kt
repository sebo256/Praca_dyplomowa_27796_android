package com.praca.dyplomowa.android.api.request

data class JobRequestUpdate(
    val objectId: String,
    val companyName: String?,
    val name: String,
    val surname: String,
    val street: String,
    val postalCode: String?,
    val city: String,
    val phoneNumber: String?,
    val email: String?,
    val subject: String,
    val jobType: String,
    val plannedDate: Long?,
    val timeSpent: Int,
    val note: String?,
    val isCompleted: Boolean,
)
