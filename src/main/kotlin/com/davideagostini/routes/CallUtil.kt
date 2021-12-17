import com.davideagostini.plugins.email
import com.davideagostini.plugins.userId

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

val ApplicationCall.userId: String
    get() = principal<JWTPrincipal>()?.userId.toString()

val ApplicationCall.email: String
    get() = principal<JWTPrincipal>()?.email.toString()