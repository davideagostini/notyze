package com.davideagostini.service

import com.davideagostini.data.models.Category
import com.davideagostini.data.repository.category.CategoryRepository
import com.davideagostini.data.requests.CreateCategoryRequest

class CategoryService(
    private val categoryRepository: CategoryRepository,
) {

    suspend fun createCategory(request: CreateCategoryRequest, userId: String): Boolean {
        if (request.id != null) {
            return categoryRepository.createCategory(
                Category(
                    title = request.title,
                    color = request.color,
                    userId = userId,
                    id = request.id,
                )
            )
        } else {
            return categoryRepository.createCategory(
                Category(
                    title = request.title,
                    color = request.color,
                    userId = userId,
                )
            )
        }
    }

    suspend fun getCategory(categoryId: String): Category? {
        return categoryRepository.getCategory(categoryId)
    }

    suspend fun getCategoryDetails(ownUserId: String, categoryId: String): Category? {
        return categoryRepository.getCategoryDetails(ownUserId, categoryId)
    }

    suspend fun deleteCategory(categoryId: String): Boolean {
        return categoryRepository.deleteCategory(categoryId)
    }

    suspend fun getCategoriesForOwner(ownUserId: String): List<Category> {
        return categoryRepository.getCategoriesForOwner(ownUserId)
    }
}