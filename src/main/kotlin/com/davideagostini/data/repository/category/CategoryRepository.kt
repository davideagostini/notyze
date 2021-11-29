package com.davideagostini.data.repository.category

import com.davideagostini.data.models.Category
import com.davideagostini.data.responses.CategoryResponse

interface CategoryRepository {

    suspend fun createCategory(category: Category): Boolean

    suspend fun deleteCategory(categoryId: String)

    suspend fun getCategory(categoryId: String): Category?

    suspend fun getCategoryDetails(userId: String, categoryId: String): CategoryResponse?

    suspend fun getCategoriesForOwner(ownUserId: String): List<Category>

}