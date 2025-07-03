package com.example.demo.domain.port

import com.example.demo.domain.model.Book
import com.example.demo.infrastructure.driving.controller.dto.BookDTO
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : BookPort {
    override fun findAll(): List<Book> {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM demo", MapSqlParameterSource()) { rs, _ ->
                Book(author = rs.getString("author"), title = rs.getString("title"))
            }
    }

    override fun insert(book: Book) {
        namedParameterJdbcTemplate
            .update(
                "INSERT INTO demo (title, author) VALUES (:title, :author)", mapOf(
                    "author" to book.author, "title" to book.title
                )
            )
    }
}