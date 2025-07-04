package com.example.demo.infrastructure.driving.controller

import com.example.demo.domain.model.Book
import com.example.demo.domain.usecase.BookUseCase
import com.example.demo.infrastructure.driven.adapter.BookDAO
import com.example.demo.infrastructure.driving.controller.dto.BookDTO
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.verify
import io.mockk.every
import io.mockk.justRun
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@WebMvcTest(BookController::class)
class BookControllerTest(val mockMvc: MockMvc, @MockkBean val bookUC: BookUseCase): FunSpec(
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
                    content { contentType(APPLICATION_JSON) }
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
        test("POST /books route") {
            justRun { bookUC.insert(any()) }
            val result = mockMvc.post("/books") {
                content = """
                {
                  "title": "Les misérables",
                  "author": "Victor Hugo"
                }
            """.trimIndent()
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isCreated() }
            }
            val expected = Book(
                title = "Les misérables",
                author = "Victor Hugo"
            )

            verify(exactly = 1) { bookUC.insert(any()) }
        }

        // ****** NOT WORKING WHEN I ADD THE ERROR HANDLER ****** //
        test("Invalid input POST /books route") {
            justRun { bookUC.insert(any()) }
            val result = mockMvc.post("/books") {
                content = """
                {
                  "MAUVAIS TRUC MACHIN": "Les misérables",
                  "author": "Victor Hugo"
                }
            """.trimIndent()
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isBadRequest() }
            }
        }

        // ****** NOT WORKING WHEN I REMOVE THE ERROR HANDLER ****** //
        test("Domain exception: findAll throws → should return 500") {
            justRun { bookUC.reserveBook(any(), any()) }

            val result = mockMvc.get("/books")
                .andExpect {
                    status { isInternalServerError() }
                }
                .andReturn()
        }

        test("POST /books/reserve route") {
            justRun { bookUC.reserveBook(any(), any()) }

            mockMvc.post("/books/reserve") {
                content = """
            {
              "title": "Les misérables",
              "author": "Victor Hugo"
            }
        """.trimIndent()
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk() }
            }
        }

    })
