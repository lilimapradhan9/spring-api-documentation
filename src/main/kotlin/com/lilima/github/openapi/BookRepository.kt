package com.lilima.github.openapi

import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class BookRepository {
    private val books = mutableListOf<Book>()

    fun findAll(): Mono<MutableList<Book>> {
        return Mono.just(books)
    }

    fun addBook(book: Book): Mono<Book> {
        return Mono.just(books.add(book))
            .map { book }
    }

}
