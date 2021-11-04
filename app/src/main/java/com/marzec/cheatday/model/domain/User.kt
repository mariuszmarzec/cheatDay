package com.marzec.cheatday.model.domain

import com.marzec.cheatday.db.model.db.UserEntity

data class User(
    val id: Long,
    val email: String
)

fun UserEntity.toDomain() = User(id, email)
