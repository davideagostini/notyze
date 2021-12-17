package com.davideagostini.data.requests

import com.davideagostini.data.models.Category

data class CreateNoteRequest(
    val title: String,
    val description: String,
    val date: Long,
    val collaborators: List<String>? = null,
    val categories: List<Category>? = null,
    val id: String? = null,
)
