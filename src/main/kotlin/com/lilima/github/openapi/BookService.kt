package com.lilima.github.openapi

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class BookService(
    private val bookRepository: BookRepository
) {
    fun getAllBooks(): Mono<MutableList<Book>> {
        return bookRepository.findAll()
    }

    fun addBook(bookDto: BookDto): Mono<Book> {
        val book = jacksonObjectMapper().convertValue<Book>(bookDto)
        return bookRepository.addBook(book)
    }
}
