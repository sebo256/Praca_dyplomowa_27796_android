package com.praca.dyplomowa.android.api.response

data class RefreshTokenResponse(
    val status: Boolean,
    val token: String,
    val message: String
)
