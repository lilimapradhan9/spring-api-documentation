package com.lilima.github

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/books")
class BooksController(
    private val bookService: BookService
) {

    @GetMapping
    fun getAllBooks(): Mono<MutableList<Book>> {
        return bookService.getAllBooks()
    }

    @PostMapping
    fun addBook(
        @RequestBody bookDto: BookDto
    ): Mono<Book> {
        return bookService.addBook(bookDto)
    }
}