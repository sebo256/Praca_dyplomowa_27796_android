package com.praca.dyplomowa.android.api.request

data class JobAddTimeSpentRequest(
    val objectId: String,
    val timeSpentMap: MutableMap<String, Int>
)
