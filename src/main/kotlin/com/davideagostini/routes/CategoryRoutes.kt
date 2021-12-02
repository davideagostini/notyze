package com.davideagostini.routes

import com.davideagostini.data.requests.CreateCategoryRequest
import com.davideagostini.data.responses.BasicApiResponse
import com.davideagostini.data.util.ApiResponseMessages.INTERNAL_SERVER_ERROR
import com.davideagostini.service.CategoryService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import userId

fun Route.createCategory(categoryService: CategoryService) {
    authenticate {
        post("/api/category/create") {
            val request = call.receiveOrNull<CreateCategoryRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userId = call.userId
            val createCategoryAcknowledged = categoryService.createCategory(request, userId)
            if (createCategoryAcknowledged) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = INTERNAL_SERVER_ERROR
                    )
                )
            }
        }
    }
}

fun Route.getCategoriesForOwner(categoryService: CategoryService) {
    authenticate {
        get("/api/category/all") {
            val categories = categoryService.getCategoriesForOwner(call.userId)
            call.respond(HttpStatusCode.OK, categories)
        }
    }
}


fun Route.deleteCategory(categoryService: CategoryService) {
    authenticate {
        delete("/api/category/delete/{categoryId}") {
            val categoryId = call.parameters["categoryId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val category = categoryService.getCategory(categoryId)
            if (category == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(
                        successful = false
                    )
                )
                return@delete
            }

            if (category.userId != call.userId) {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }
            categoryService.deleteCategory(categoryId)
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = true,
                )
            )

        }
    }
}

fun Route.getCategoryDetails(categoryService: CategoryService) {
    authenticate {
        get("/api/category/details/{categoryId}") {
            val categoryId = call.parameters["categoryId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val category = categoryService.getCategoryDetails(call.userId, categoryId) ?: kotlin.run {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            if (category.userId != call.userId) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            call.respond(
                HttpStatusCode.OK,
                category
            )
        }
    }
}