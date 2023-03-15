package com.praca.dyplomowa.android.api.request

data class RegistrationRequest(
    val username: String,
    val password: String,
    val name: String,
    val surname: String
)
