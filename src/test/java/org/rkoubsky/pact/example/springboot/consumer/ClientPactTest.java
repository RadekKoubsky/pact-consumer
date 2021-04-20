package org.rkoubsky.pact.example.springboot.consumer;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.rkoubsky.pact.example.springboot.consumer.Client.PUBLISHED_DATE_FORMAT;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.remondis.cdc.consumer.pactbuilder.ConsumerExpects;
import com.remondis.cdc.consumer.pactbuilder.PactDslModifier;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "book_provider", hostInterface = "localhost",  port = "8080")
public class ClientPactTest {

    public static final String ISBN = "453-1449373320";

    @Pact(consumer = "book_consumer")
    public RequestResponsePact pactForValidBook(PactDslWithProvider builder) {
        return builder
                .given("book exists for given isbn")
                .uponReceiving("a request for book data")
                .path("/book")
                .method("GET")
                .query("isbn=" + ISBN)
                .willRespondWith()
                .status(200)
                .body(
                        ConsumerExpects.type(Book.class)
                                       .useTypeMapping(LocalDate.class, localDateMapping())
                                       .build(getBookSample())
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactForValidBook")
    public void testValidBook(MockServer mockProvider) {
        Client client = new Client(mockProvider.getUrl());

        assertThat(client.getBook(ISBN)).isEqualTo(getBookSample());

    }

    @Pact(consumer = "book_consumer")
    public RequestResponsePact pactForWhereThenIsNoBook(PactDslWithProvider builder) {
        return builder
                .given("book does not exist for given isbn")
                .uponReceiving("a request for book data")
                .path("/book")
                .method("GET")
                .query("isbn=" + ISBN)
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactForWhereThenIsNoBook")
    public void testBookDoesNotExistForGivenIsbn(MockServer mockProvider) {
        Client client = new Client(mockProvider.getUrl());
        assertThatThrownBy(() -> client.getBook(ISBN))
                .isExactlyInstanceOf(WebClientResponseException.NotFound.class);

    }

    private PactDslModifier<LocalDate> localDateMapping() {
        return (pactDslJsonBody, fieldName, fieldValue) -> {
            return pactDslJsonBody.date(fieldName, PUBLISHED_DATE_FORMAT,
                                        Date.from(fieldValue.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        };
    }

    private Book getBookSample() {
        return Book.builder()
                   .title("Designing Data-Intensive Applications")
                   .author("Martin Kleppmann")
                   .isbn("978-1449373320")
                   .published(LocalDate.of(2017, 4, 28))
                   .build();
    }
}
