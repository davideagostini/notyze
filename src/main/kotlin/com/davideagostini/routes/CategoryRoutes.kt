package com.davideagostini.routes

import com.davideagostini.data.requests.CreateCategoryRequest
import com.davideagostini.data.requests.DeleteCategoryRequest
import com.davideagostini.data.responses.BasicApiResponse
import com.davideagostini.data.util.ApiResponseMessages.FIELDS_BLANK
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
            when(categoryService.createCategory(request, userId)) {
                is CategoryService.ValidationEvent.ErrorFieldEmpty ->  {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = FIELDS_BLANK
                        )
                    )
                }
                is CategoryService.ValidationEvent.Success -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                }
                is CategoryService.ValidationEvent.UserNotFound -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = "User not found"
                        )
                    )
                }
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
    authenticate{
        delete("/api/category/delete") {
            val request = call.receiveOrNull<DeleteCategoryRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val category = categoryService.getCategory(request.categoryId)
            if (category == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            if (category.userId == call.userId) {
                categoryService.deleteCategory(request.categoryId)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

fun Route.getCategoryDetails(categoryService: CategoryService) {
    authenticate {
        get("/api/category/details") {
            val categoryId = call.parameters["categoryId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val post = categoryService.getCategoryDetails(call.userId, categoryId) ?: kotlin.run {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = post
                )
            )
        }
    }
}