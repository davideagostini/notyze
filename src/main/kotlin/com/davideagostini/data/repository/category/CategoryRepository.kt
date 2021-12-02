package com.davideagostini.data.repository.category

import com.davideagostini.data.models.Category

interface CategoryRepository {

    suspend fun createCategory(category: Category): Boolean

    suspend fun deleteCategory(categoryId: String): Boolean

    suspend fun getCategory(categoryId: String): Category?

    suspend fun getCategoryDetails(userId: String, categoryId: String): Category?

    suspend fun getCategoriesForOwner(ownUserId: String): List<Category>

}