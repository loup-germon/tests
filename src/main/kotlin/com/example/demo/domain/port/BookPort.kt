package com.example.demo.domain.port

import com.example.demo.domain.model.Book


interface BookPort {
    fun findAll(): List<Book>
    fun insert(value: Book)
    fun reserve(author: String, title: String)
    fun isReserved(author:String, title: String): Boolean
    fun bookExists(author:String, title: String): Boolean
}