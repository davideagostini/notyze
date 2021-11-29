package com.davideagostini.data.repository.category

import com.davideagostini.data.models.Category
import com.davideagostini.data.models.User
import com.davideagostini.data.responses.CategoryResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class CategoryRepositoryImpl(
    db: CoroutineDatabase
): CategoryRepository {

    private val categories = db.getCollection<Category>()
    private val users = db.getCollection<User>()

    override suspend fun createCategory(category: Category): Boolean {
        val categoryExists = categories.findOneById(category.id) != null
        return if (categoryExists) {
            categories.updateOneById(category.id, category).wasAcknowledged()
        } else {
            categories.insertOne(category).wasAcknowledged()
        }
    }

    override suspend fun deleteCategory(categoryId: String) {
        categories.deleteOneById(categoryId)
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return categories.findOneById(categoryId)
    }

    override suspend fun getCategoryDetails(userId: String, categoryId: String): CategoryResponse? {
        val category =  categories.findOneById(categoryId) ?: return null
        val user = users.findOneById(category.userId) ?: return null
        return CategoryResponse(
            id = category.id,
            userId = user.id,
            title = category.title,
            color = category.color
        )
    }

    override suspend fun getCategoriesForOwner(ownUserId: String): List<Category> {
        return categories.find(Category::userId eq ownUserId).toList()
    }

}