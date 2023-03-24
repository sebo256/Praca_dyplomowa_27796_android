package com.praca.dyplomowa.android.api.response

data class JobGetAllResponse(
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
    val jobType: String,
    val dateOfCreation: Long,
    val plannedDate: Long,
    val timeSpent: Int,
    val note: String,
    val isCompleted: Boolean,
    val createdBy: UserResponse,
    val jobAppliedTo: Collection<String>
)

data class UserResponse(
    val username: String,
    val name: String,
    val surname: String
)

data class JobGetAllResponseCollection(
    val collection: Collection<JobGetAllResponse>
)
