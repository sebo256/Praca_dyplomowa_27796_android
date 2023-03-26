package com.praca.dyplomowa.android.api.response

data class LoginResponse(
    val jwt: String?,
    val message: String,
    var username: String?,
    val roles: Collection<String>?
)
