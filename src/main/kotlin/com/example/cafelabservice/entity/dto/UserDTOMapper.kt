package com.example.cafelabservice.entity.dto

import com.example.cafelabservice.entity.User
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class UserDTOMapper : Converter<User, UserDTO> {
    override fun convert(user: User) = UserDTO(
        id = user.id,
        username = user.username ?: "",
        email = user.email,
        password = user.password ?: "",
    )
}