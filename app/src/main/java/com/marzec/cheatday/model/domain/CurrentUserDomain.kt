package com.marzec.cheatday.model.domain

data class CurrentUserDomain(
    val id: Int,
    val auth: String,
    val email: String
)

fun CurrentUserDomain.toUser() = User(
    id.toLong(), email
)
