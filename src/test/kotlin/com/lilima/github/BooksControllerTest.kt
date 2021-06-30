package com.lilima.github

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.BodyInserters

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksControllerTest(
    @Autowired
    val client: WebTestClient
) {

    @Test
    fun `add a book and return all books`() {
        client.post().uri("books")
            .body(BodyInserters.fromValue(BookDto("abc", "xyz")))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody<Book>()
            .returnResult().responseBody!!

        val result = client.get().uri("/books")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody<List<Book>>()
            .returnResult().responseBody!!

        result shouldBe listOf(Book("abc", "xyz"))
    }
}