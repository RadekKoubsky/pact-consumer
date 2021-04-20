package org.rkoubsky.pact.example.springboot.consumer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private String title;
    private String author;
    private String isbn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Client.PUBLISHED_DATE_FORMAT)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate published;
}
