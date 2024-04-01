package org.example.dto;

public record BookModelDto(
        String title,
        String isbn,
        Integer quantity,
        String author) {
}
