package org.example.service;


import com.example.bookstore.grpc.Book;
import com.example.bookstore.grpc.BookIdRequest;
import com.example.bookstore.grpc.BookServiceGrpc;
import com.example.bookstore.grpc.CreateBookRequest;
import com.example.bookstore.grpc.Deleted;
import com.example.bookstore.grpc.UpdateBookRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.exception.BookNotFoundException;
import org.example.exception.DuplicateUniqueFieldException;
import org.example.mapper.BookMapper;
import org.example.model.BookModel;
import org.example.repository.BookRepository;

import java.util.Optional;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public void createBook(CreateBookRequest request, StreamObserver<Book> responseObserver) {
        Optional<BookModel> bookModelByIsbn = bookRepository.findBookModelByIsbn(request.getBook().getIsbn());

        if (bookModelByIsbn.isPresent()) {
            throw new DuplicateUniqueFieldException(
                    String.format("Book with isbn: %s already exists", request.getBook().getIsbn()));
        }

        BookModel book = bookMapper.toModel(request.getBook());
        BookModel savedBook = bookRepository.save(book);

        Book response = Book.newBuilder()
                .setId(savedBook.getId().toString())
                .setAuthor(savedBook.getAuthor())
                .setIsbn(savedBook.getIsbn())
                .setQuantity(savedBook.getQuantity())
                .setTitle(savedBook.getTitle())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBook(BookIdRequest request, StreamObserver<Book> responseObserver) {
        Optional<BookModel> optionalBook = bookRepository.findById(UUID.fromString(request.getId()));

        if (optionalBook.isPresent()) {
            BookModel book = optionalBook.get();

            Book response = Book.newBuilder()
                    .setId(book.getId().toString())
                    .setAuthor(book.getAuthor())
                    .setIsbn(book.getIsbn())
                    .setQuantity(book.getQuantity())
                    .setTitle(book.getTitle())
                    .build();
            responseObserver.onNext(response);

        } else {
            throw new BookNotFoundException(String.format("Can't find book with id: %s", request.getId()));
        }

        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<Book> responseObserver) {
        Optional<BookModel> optionalBook = bookRepository.findById(UUID.fromString(request.getBook().getId()));

        if (optionalBook.isPresent()) {
            BookModel book = bookMapper.toModelWithId(request.getBook());
            bookRepository.save(book);
            responseObserver.onNext(request.getBook());
        } else {
            throw new BookNotFoundException(String.format("Can't find book with id: %s", request.getBook().getId()));
        }

        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(BookIdRequest request, StreamObserver<Deleted> responseObserver) {
        Optional<BookModel> optionalBook = bookRepository.findById(UUID.fromString(request.getId()));

        Deleted.Builder responseBuilder = Deleted.newBuilder();

        if (optionalBook.isPresent()) {
            BookModel book = optionalBook.get();
            bookRepository.deleteById(book.getId());
            responseBuilder.setStatus(Deleted.Status.SUCCESS);
        } else {
            throw new BookNotFoundException(String.format("Can't find book with id: %s", request.getId()));
        }

        Deleted deleted = responseBuilder.build();
        responseObserver.onNext(deleted);
        responseObserver.onCompleted();
    }

}