package com.example.demo.infrastructure.application

import com.example.demo.infrastructure.driven.adapter.BookDAO
import com.example.demo.domain.usecase.BookUseCase
import org.springframework.context.annotation.Bean

class BookUseCasesConfiguration {
    @Bean
    fun BookUseCase(dependency: BookDAO) : BookUseCase {
        return BookUseCase(dependency)
    }
}