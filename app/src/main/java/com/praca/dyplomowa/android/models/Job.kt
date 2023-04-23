package com.praca.dyplomowa.android.models

data class Job(
    val id: String,
    val client: Client,
    val subject: String,
    val jobType: JobType,
    val dateOfCreation: Long,
    val plannedDate: Long,
    val note: String,
    val isCompleted: Boolean,
    val createdBy: User,
    val jobAppliedTo: Collection<String>? = null,
    val timeSpent: MutableMap<String, Int>? = mutableMapOf()
)