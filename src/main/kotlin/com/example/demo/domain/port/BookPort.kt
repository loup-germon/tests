package com.example.demo.domain.port

import com.example.demo.domain.model.Book


interface BookPort {
    fun findAll(): List<Book>
    fun insert(value: Book)
}