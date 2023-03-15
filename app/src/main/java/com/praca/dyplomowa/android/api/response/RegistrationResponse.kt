package com.praca.dyplomowa.android.api.response

data class RegistrationResponse(
        val status: Boolean,
        val message: String,
        val account: RegistrationSuccess?
)

data class RegistrationSuccess(
        val id: ObjectId,
        var username: String
)

data class ObjectId(
        val timestamp: Int,
        val date: String
)
