package com.praca.dyplomowa.android.api.request

data class JobApplyToRequest(
    val objectId: String,
    val jobAppliedTo: Collection<String>
)
