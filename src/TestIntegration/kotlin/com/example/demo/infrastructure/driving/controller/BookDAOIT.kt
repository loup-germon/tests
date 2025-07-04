package com.example.demo.infrastructure.driving.controller

import com.example.demo.domain.model.Book
import com.example.demo.infrastructure.driven.adapter.BookDAO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
@Transactional
@SpringBootTest
@ActiveProfiles("testIntegration")
class BookDAOIT(@Autowired bookDAO: BookDAO, @Autowired private val jdbcTemplate: NamedParameterJdbcTemplate) : FunSpec(
    {
        beforeEach {
            // Clean the table before each test to isolate them
            jdbcTemplate.update("DELETE FROM books", emptyMap<String, Any>())
        }

        test("Inserting and retrieving book with findAll") {
            println("Datasource = " + System.getProperty("spring.datasource.url"))
            val book = Book(title = "Test Book", author = "Test Author")
            bookDAO.insert(book)

            val books = bookDAO.findAll()
            books.shouldHaveSize(1)
            books.first().title shouldBe "Test Book"
            books.first().author shouldBe "Test Author"
        }

        test("FindAll on empty table should return empty list") {
            val books = bookDAO.findAll()
            books.shouldBeEmpty()
        }

        test("Books are inserted unreserved by default"){
            val book = Book(title = "Test Book", author = "Test Author")
            bookDAO.insert(book)
            val books = bookDAO.findAll()
            books.first().reserved shouldBe false
        }

        test("Reserving a book"){
            val book = Book(title = "Test Book", author = "Test Author")
            bookDAO.insert(book)
            bookDAO.findAll().first().reserved shouldBe false

            bookDAO.reserve(title = "Test Book", author = "Test Author")
            val books = bookDAO.findAll()
            books.first().reserved shouldBe true
        }
        test("Book is Reserved") {
            val book = Book(title = "Test Book", author = "Test Author")
            bookDAO.insert(book)
            bookDAO.isReserved(title = "Test Book", author = "Test Author") shouldBe false
            bookDAO.reserve(title = "Test Book", author = "Test Author")
            bookDAO.isReserved(title = "Test Book", author = "Test Author") shouldBe true
        }

        test("Book exists") {
            val book = Book(title = "Test Book", author = "Test Author")
            bookDAO.bookExists(title = "Test Book", author = "Test Author") shouldBe false
            bookDAO.insert(book)
            bookDAO.bookExists(title = "Test Book", author = "Test Author") shouldBe true
        }
    }
) {
    init {
        extension(SpringExtension)
        afterSpec {
            container.stop()
        }
    }
    companion object {
        private val container = PostgreSQLContainer<Nothing>("postgres:13-alpine")
        init {
            container.start()
            System.setProperty("spring.datasource.url", container.jdbcUrl)
            System.setProperty("spring.datasource.username", container.username)
            System.setProperty("spring.datasource.password", container.password)
        }
    }

}