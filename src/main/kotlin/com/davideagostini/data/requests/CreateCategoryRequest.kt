package com.davideagostini.data.requests

data class CreateCategoryRequest(
    val title: String,
    val color: String,
    val id: String? = null,
)