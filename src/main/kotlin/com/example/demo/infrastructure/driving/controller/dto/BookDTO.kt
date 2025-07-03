package com.example.demo.infrastructure.driving.controller.dto

import com.example.demo.domain.model.Book

data class BookDTO(
    val title: String,
    val author: String


)
fun Book.toDTO() = BookDTO(
    title = this.title,
    author = this.author
)
