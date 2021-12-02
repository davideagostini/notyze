package com.davideagostini.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateCategoryRequest(
    val title: String,
    val color: String,
    val id: String? = null,
)