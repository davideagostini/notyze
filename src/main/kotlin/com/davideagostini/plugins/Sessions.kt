package com.davideagostini.plugins

import com.davideagostini.service.NotyzeSession
import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<NotyzeSession>("SESSION")
    }
    intercept(ApplicationCallPipeline.Features) {
        if(call.sessions.get<NotyzeSession>() == null) {
            val userId = call.parameters["userId"] ?: return@intercept
            call.sessions.set(NotyzeSession(userId, generateNonce()))
        }
    }
}