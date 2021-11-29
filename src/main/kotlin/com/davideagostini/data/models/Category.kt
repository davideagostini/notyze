package com.davideagostini.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Category(
    val title: String,
    val color: String = "#ff0000",
    val userId: String,
    @BsonId
    val id: String = ObjectId().toString()
)
