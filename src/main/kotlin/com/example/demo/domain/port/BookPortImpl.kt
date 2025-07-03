package com.example.demo.domain.port

import com.example.demo.domain.model.Book
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class BookPortImpl : BookPort {
    override fun findAll(): List<Book> {
        val list = mutableListOf<Book>()
        return list
    }

    override fun insert(value: Book) {
    }
}