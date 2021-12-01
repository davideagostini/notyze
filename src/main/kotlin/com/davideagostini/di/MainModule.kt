package com.davideagostini.di

import com.davideagostini.data.repository.category.CategoryRepository
import com.davideagostini.data.repository.category.CategoryRepositoryImpl
import com.davideagostini.data.repository.user.UserRepository
import com.davideagostini.data.repository.user.UserRepositoryImpl
import com.davideagostini.data.util.Constants
import com.davideagostini.service.CategoryService
import com.davideagostini.service.UserService
import com.google.gson.Gson
import org.koin.dsl.module

import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<CategoryRepository> {
        CategoryRepositoryImpl(get())
    }


    single { UserService(get()) }
    single { CategoryService(get()) }

    single { Gson() }
}