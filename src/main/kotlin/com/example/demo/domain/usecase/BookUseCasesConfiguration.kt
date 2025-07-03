package com.example.demo.domain.usecase

import com.example.demo.domain.port.BookPort
import org.springframework.context.annotation.Bean

class BookUseCasesConfiguration {
    @Bean
    fun BookUseCase(dependency: BookPort) : BookUseCase {
        return BookUseCase(dependency)
    }
}