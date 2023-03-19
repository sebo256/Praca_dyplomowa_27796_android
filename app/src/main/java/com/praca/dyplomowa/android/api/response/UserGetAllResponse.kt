package com.praca.dyplomowa.android.api.response

data class UserGetAllResponse(
    val id: String,
    val username: String,
    val name: String,
    val surname: String,
)

data class UserGetAllResponseCollection(
    val collection: Collection<UserGetAllResponse>
)
