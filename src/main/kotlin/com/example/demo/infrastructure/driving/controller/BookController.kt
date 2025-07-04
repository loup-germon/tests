package com.example.demo.infrastructure.driving.controller

import com.example.demo.domain.usecase.BookUseCase
import com.example.demo.infrastructure.driving.controller.dto.BookDTO
import com.example.demo.infrastructure.driving.controller.dto.toDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController (val bookUseCase : BookUseCase){
    @GetMapping
    @CrossOrigin
    fun findAll(): List<BookDTO> {return bookUseCase.findAll().map { it.toDTO() }  }

    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    fun insertBook(@RequestBody bookDTO: BookDTO) {
        //println(bookDTO.toDomain())
        bookUseCase.insert(bookDTO.toDomain())
    }

    /*
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException() {

    }*/

    @PostMapping("/reserve")
    fun reserveBook(@RequestBody bookDTO: BookDTO)
    {
        bookUseCase.reserveBook(bookDTO.author, bookDTO.title)
    }

}