package com.dewival.testdewival.repository.models

import io.realm.annotations.PrimaryKey

data class Photo(
    val name: String,
    val created: Long,
    val send: Long
)