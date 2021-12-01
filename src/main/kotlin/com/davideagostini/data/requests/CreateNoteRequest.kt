package com.davideagostini.data.requests

data class CreateNoteRequest(
    val title: String,
    val description: String,
    val date: Long,
    val userId: String,
    val collaborators: List<String>? = null,
    val categories: List<String>? = null,
    val id: String? = null,
)
