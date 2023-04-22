package com.praca.dyplomowa.android.api.request

data class ClientRequestUpdate(
    val objectId: String,
    val companyName: String?,
    val name: String,
    val surname: String,
    val street: String,
    val postalCode: String?,
    val city: String,
    val phoneNumber: String?,
    val email: String?
)
