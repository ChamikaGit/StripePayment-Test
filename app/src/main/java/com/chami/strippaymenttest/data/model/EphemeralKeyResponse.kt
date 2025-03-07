package com.chami.strippaymenttest.data.model

data class EphemeralKeyResponse(
    val id: String,
    val `object`: String,
    val associated_objects: List<AssociatedObject>,
    val created: Long,
    val expires: Long,
    val livemode: Boolean,
    val secret: String
)

data class AssociatedObject(
    val id: String,
    val type: String
)