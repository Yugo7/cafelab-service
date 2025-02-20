package com.example.cafelabservice.security

import com.example.cafelabservice.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SecurityUser(private val user: User) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> =
        user.role?.map { SimpleGrantedAuthority(it) } ?: emptyList()

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username ?: user.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
