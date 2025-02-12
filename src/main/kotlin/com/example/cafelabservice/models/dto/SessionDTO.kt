package com.example.cafelabservice.models.dto

import com.stripe.model.checkout.Session

data class SessionDTO(
    val sessionId: String,
    val url: String,
){
    companion object {
        fun fromSession(session: Session): SessionDTO {
            return SessionDTO(session.id, session.url)
        }
    }
}