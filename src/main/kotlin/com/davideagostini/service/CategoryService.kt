package com.davideagostini.service

import com.davideagostini.data.models.Category
import com.davideagostini.data.repository.category.CategoryRepository
import com.davideagostini.data.repository.user.UserRepository
import com.davideagostini.data.requests.CreateCategoryRequest
import com.davideagostini.data.responses.CategoryResponse

class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository
) {

    suspend fun createCategory(request: CreateCategoryRequest, userId: String): ValidationEvent {
        request.apply {
            if (title.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
        }
        val user = userRepository.getUserById(userId) ?: return ValidationEvent.UserNotFound
        if (request.id != null) {
            categoryRepository.createCategory(
                Category(
                    title = request.title,
                    color = request.color,
                    userId = userId,
                    id = request.id,
                )
            )
        } else {
            categoryRepository.createCategory(
                Category(
                    title = request.title,
                    color = request.color,
                    userId = userId,
                )
            )
        }


        return ValidationEvent.Success
    }

    suspend fun getCategory(categoryId: String): Category? {
        return categoryRepository.getCategory(categoryId)
    }

    suspend fun getCategoryDetails(ownUserId: String, categoryId: String): CategoryResponse? {
        return categoryRepository.getCategoryDetails(ownUserId, categoryId)
    }

    suspend fun deleteCategory(categoryId: String) {
        categoryRepository.deleteCategory(categoryId)
    }

    suspend fun getCategoriesForOwner(ownUserId: String): List<Category> {
        return categoryRepository.getCategoriesForOwner(ownUserId)
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty: ValidationEvent()
        object UserNotFound: ValidationEvent()
        object Success: ValidationEvent()
    }
}