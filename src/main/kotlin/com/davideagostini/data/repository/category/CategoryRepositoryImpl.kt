package com.davideagostini.data.repository.category

import com.davideagostini.data.models.Category
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class CategoryRepositoryImpl(
    db: CoroutineDatabase
): CategoryRepository {

    private val categories = db.getCollection<Category>()

    override suspend fun createCategory(category: Category): Boolean {
        val categoryExists = categories.findOneById(category.id) != null
        return if (categoryExists) {
            categories.updateOneById(category.id, category).wasAcknowledged()
        } else {
            categories.insertOne(category).wasAcknowledged()
        }
    }

    override suspend fun deleteCategory(categoryId: String): Boolean {
        val deletedCount = categories.deleteOneById(categoryId).deletedCount
        return deletedCount > 0
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return categories.findOneById(categoryId)
    }

    override suspend fun getCategoryDetails(userId: String, categoryId: String): Category? {
        return categories.findOne(Category::id eq categoryId, Category::userId eq userId)
    }

    override suspend fun getCategoriesForOwner(ownUserId: String): List<Category> {
        return categories.find(Category::userId eq ownUserId).toList()
    }

}