package com.davideagostini.data.responses

import com.davideagostini.data.models.Category

data class NoteResponse(
    val id: String,
    val title: String,
    val description: String,
    val collaborators: List<String>? = null,
    val categories: List<Category>? = null
)
