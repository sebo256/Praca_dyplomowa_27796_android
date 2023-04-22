package com.praca.dyplomowa.android.api.response

import com.praca.dyplomowa.android.models.Client
import com.praca.dyplomowa.android.models.JobType

data class JobGetAllResponse(
    val id: String,
    val client: Client,
    val subject: String,
    val jobType: JobType,
    val dateOfCreation: Long,
    val plannedDate: Long,
    val timeSpent: Int,
    val note: String,
    val isCompleted: Boolean,
    val createdBy: JobUserResponse,
    val jobAppliedTo: Collection<String>
)

data class JobUserResponse(
    val username: String,
    val name: String,
    val surname: String
)

data class JobGetAllResponseCollection(
    val collection: Collection<JobGetAllResponse>,
)
