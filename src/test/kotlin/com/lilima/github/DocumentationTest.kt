package com.lilima.github

import com.lilima.github.rest.documentation.BookDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@ExtendWith(RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DocumentationTest {

    private lateinit var webTestClient: WebTestClient

    @LocalServerPort
    private lateinit var port: String

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider?) {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .filter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .build()
    }


    @Test
    fun testBooksApi() {
        webTestClient
            .post()
            .uri("/books")
            .body(BodyInserters.fromValue(BookDto("abc", "xyz")))
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .consumeWith(document("books-api/post"))

        webTestClient
            .post()
            .uri("/books")
            .body(BodyInserters.fromValue("{}"))
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .consumeWith(document("books-api/post-error"))

        webTestClient
            .get()
            .uri("/books")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .consumeWith(document("books-api/get"))
    }
}