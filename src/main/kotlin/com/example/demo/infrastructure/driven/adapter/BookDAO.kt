package com.example.demo.infrastructure.driven.adapter

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : BookPort {
    override fun findAll(): List<Book> {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM books", MapSqlParameterSource())
            { rs, _ ->
                Book(author = rs.getString("author"),
                    title = rs.getString("title"),
                    reserved = rs.getBoolean("reserved"))
            }
    }

    override fun insert(book: Book) {
        namedParameterJdbcTemplate
            .update(
                "INSERT INTO books (title, author) VALUES (:title, :author)", mapOf(
                    "author" to book.author, "title" to book.title
                )
            )
    }

    override fun reserve(author: String, title: String) {
        val updated = namedParameterJdbcTemplate.update(
            """
            UPDATE books 
            SET reserved = true 
            WHERE title = :title AND author = :author
            """.trimIndent(),
            mapOf("title" to title, "author" to author)
        )
    }

    override fun isReserved(author: String, title: String): Boolean {
        return namedParameterJdbcTemplate.queryForObject(
            """
        SELECT reserved 
        FROM books 
        WHERE author = :author AND title = :title
        """.trimIndent(),
            mapOf("author" to author, "title" to title),
            Boolean::class.java
        ) ?: false
    }

    override fun bookExists(author: String, title: String): Boolean {
        return namedParameterJdbcTemplate.queryForObject(
            """
        SELECT EXISTS (
            SELECT 1 FROM books WHERE author = :author AND title = :title
        )
        """.trimIndent(),
            mapOf("author" to author, "title" to title),
            Boolean::class.java
        ) ?: false
    }
}