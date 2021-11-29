package com.davideagostini.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Note(
    val title: String,
    val description: String,
    val date: Long,
    val owners: List<String>,
    val categories: List<Category> = listOf(),
    @BsonId
    val id: String = ObjectId().toString()
)