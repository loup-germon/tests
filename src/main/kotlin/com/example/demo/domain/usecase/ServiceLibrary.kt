package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.WriteToDb

class ServiceLibrary (private val writeToDb: WriteToDb) {
    fun insert(book: Book) {
        require(book.title.trim().isNotEmpty())
        require(book.author.trim().isNotEmpty())
        writeToDb.insert(book)
    }

    fun findAll(): List<Book>  {
        val list = writeToDb.findAll()
        return list.sortedBy { it.title.lowercase() }
    }

    fun insertAll(bookList: List<Book>) {
        bookList.forEach { book -> insert(book) }
    }

}