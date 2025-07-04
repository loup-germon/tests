package com.example.demo.infrastructure.driving.controller.dto

import com.example.demo.domain.model.Book

data class BookDTO(val author: String,
                   val title: String) {
    fun toDomain() : Book {
        return Book(author, title)
    }
}


fun Book.toDTO() = BookDTO(
    author = this.author,
    title = this.title
)

