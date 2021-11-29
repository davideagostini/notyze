package com.davideagostini

import io.ktor.application.*
import com.davideagostini.plugins.*
import org.koin.ktor.ext.Koin
import com.davideagostini.di.mainModule


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    configureSecurity()
    configureSockets()
    configureSession()
    configureRouting()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}

