package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort
import org.springframework.stereotype.Service

@Service
class BookUseCase (private val bookPort: BookPort) {
    fun insert(book: Book) {
        require(book.title.trim().isNotEmpty())
        require(book.author.trim().isNotEmpty())
        bookPort.insert(book)
    }

    fun findAll(): List<Book>  {
        val list = bookPort.findAll()
        return list.sortedBy { it.title.lowercase() }
    }

    fun insertAll(bookList: List<Book>) {
        bookList.forEach { book -> insert(book) }
    }

}