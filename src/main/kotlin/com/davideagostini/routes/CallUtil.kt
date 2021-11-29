import com.davideagostini.plugins.userId

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

val ApplicationCall.userId: String
    get() = principal<JWTPrincipal>()?.userId.toString()