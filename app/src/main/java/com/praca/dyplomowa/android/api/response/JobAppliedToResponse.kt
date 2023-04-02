package com.praca.dyplomowa.android.api.response

data class JobAppliedToResponse(
    val id: String,
    val status: Boolean,
    val message: String,
    val jobAppliedTo: Collection<String>
)
