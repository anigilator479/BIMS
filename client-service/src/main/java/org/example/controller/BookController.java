package org.example.controller;

import com.example.bookstore.grpc.Deleted;
import com.google.protobuf.Descriptors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.BookModelDto;
import org.example.service.BookClientService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
//1+1
@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookClientService bookClientService;

    @Operation(summary = "Get book by bookId", description = "Get a book by bookId")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<Descriptors.FieldDescriptor, Object> getBook(@PathVariable("id") String id) {
        return bookClientService.getBook(id);
    }

    @Operation(summary = "Create book", description = "Creates a new book in db")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<Descriptors.FieldDescriptor, Object> createBook(@RequestBody BookModelDto book) {
        return bookClientService.createBook(book);
    }

    @Operation(summary = "Delete book by bookId", description = "Deletes a book by bookId")
    @DeleteMapping(value = "/{id}")
    public Deleted.Status deleteBook(@PathVariable("id") String id) {
        return bookClientService.deleteBook(id);
    }

    @Operation(summary = "Update book info by bookId",
            description = "Updates data about book in the db by bookId")
    @PutMapping(value = "/{id}")
    public Map<Descriptors.FieldDescriptor, Object> updateBook(
            @RequestBody BookModelDto book, @PathVariable("id") String id) {
        return bookClientService.updateBook(book, id);
    }
}
