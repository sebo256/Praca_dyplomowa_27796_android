package com.praca.dyplomowa.android.models

data class Job(
    val id: String,
    val companyName: String,
    val name: String,
    val surname: String,
    val street: String,
    val postalCode: String,
    val city: String,
    val phoneNumber: String,
    val email: String,
    val subject: String,
    val dateOfCreation: Long,
    val plannedDate: Long,
    val timeSpent: Int,
    val note: String,
    val isCompleted: Boolean,
    val createdBy: User,
    val jobAppliedTo: Collection<String>? = null
)
