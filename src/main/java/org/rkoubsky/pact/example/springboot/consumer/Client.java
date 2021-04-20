package org.rkoubsky.pact.example.springboot.consumer;

import org.springframework.web.reactive.function.client.WebClient;

public class Client {
    public static final String PUBLISHED_DATE_FORMAT = "yyyy-MM-dd";

    private final WebClient webClient;

    public Client(String url) {
        this.webClient = WebClient.create(url);
    }

    public Book getBook(String isbn) {
        return webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("book")
                                                     .queryParam("isbn", isbn)
                                                     .build())
                        .retrieve()
                        .bodyToMono(Book.class)
                        .block();
    }
}
