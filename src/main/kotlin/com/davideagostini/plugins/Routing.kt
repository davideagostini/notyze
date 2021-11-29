package com.davideagostini.plugins

import com.davideagostini.routes.*
import com.davideagostini.service.CategoryService
import com.davideagostini.service.UserService
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userService: UserService by inject()
    val categoryService: CategoryService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        authenticate()
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )
        authenticate()

        createCategory(categoryService)
        deleteCategory(categoryService)
        getCategoryDetails(categoryService)
        getCategoriesForOwner(categoryService)
    }
}
