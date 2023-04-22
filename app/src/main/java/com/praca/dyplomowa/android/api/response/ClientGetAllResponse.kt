package com.praca.dyplomowa.android.api.response

data class ClientGetAllResponse(
    val id: String,
    val companyName: String?,
    val name: String,
    val surname: String,
    val street: String,
    val postalCode: String?,
    val city: String,
    val phoneNumber: String?,
    val email: String?
)

data class ClientGetAllResponseCollection(
    val collection: Collection<ClientGetAllResponse>
)
