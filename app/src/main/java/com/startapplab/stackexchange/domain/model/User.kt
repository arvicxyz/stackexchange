package com.startapplab.stackexchange.domain.model

data class User(
    val id: Int,
    val username: String,
    val reputation: Int,
    val location: String?,
    val creationDate: String?
)
