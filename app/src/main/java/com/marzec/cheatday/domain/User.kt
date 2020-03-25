package com.marzec.cheatday.domain

import com.marzec.cheatday.db.model.db.UserEntity

data class User(
    val uuid: String,
    val email: String
)

fun UserEntity.toDomain() = User(uuid, email)