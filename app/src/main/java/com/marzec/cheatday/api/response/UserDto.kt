package com.marzec.cheatday.api.response

import com.marzec.cheatday.model.domain.User

data class UserDto(val id: Int, val email: String)

fun UserDto.toDomain() = User(id = id.toLong(), email = email)
