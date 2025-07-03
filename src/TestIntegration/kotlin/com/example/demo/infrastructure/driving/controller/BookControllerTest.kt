package com.example.demo.infrastructure.driving.controller

import com.example.demo.domain.model.Book
import com.example.demo.domain.usecase.BookUseCase
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.Test
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.client.HttpServerErrorException

@WebMvcTest(BookController::class)
class BookControllerTest(@Autowired val mockMvc: MockMvc, @MockkBean val bookUC: BookUseCase): FunSpec(
    {

        test("use case spring test") {
            every { bookUC.findAll() } returns listOf(Book(title = "Test Book", author = "Test Author"))

            bookUC.findAll().shouldHaveSize(1)
            //bookUC.insert(Book(title = "Test Book2", author = "Test Author2"))
        }

        test("MVC controller test") {
            every { bookUC.findAll() } returns listOf(Book(title = "Test Book", author = "Test Author"))
            //val result = mockMvc.get("/books")
            val result = mockMvc.get("/books")
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    content {
                        json(
                            """
                            [
                            {
                            "title": "Test Book",
                            "author": "Test Author"
                            }
                            ]
                            """.trimIndent()
                        )
                    }

                }
                .andReturn()
        }
        test("Invalid input: POST /books with empty JSON should return 400") {
            val result = mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = "{}"
            }
                .andExpect {
                    status { isBadRequest() }
                }
                .andReturn()
        }

        test("Domain exception: findAll throws → should return 500") {
            every { bookUC.findAll() } throws RuntimeException("Simulated failure")

            val result = mockMvc.get("/books")
                .andExpect {
                    status { is5xxServerError() }
                }
                .andReturn()
        }

    })
