package org.example.service;

import com.example.bookstore.grpc.Book;
import com.example.bookstore.grpc.BookIdRequest;
import com.example.bookstore.grpc.BookServiceGrpc;
import com.example.bookstore.grpc.CreateBookRequest;
import com.example.bookstore.grpc.CustomError;
import com.example.bookstore.grpc.Deleted;
import com.example.bookstore.grpc.UpdateBookRequest;
import com.google.protobuf.Descriptors;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.dto.BookModelDto;
import org.example.exception.BookNotFoundException;
import org.example.exception.DuplicateUniqueFieldException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookClientService {

    @GrpcClient("grpc-bookstore-service")
    BookServiceGrpc.BookServiceBlockingStub synchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getBook(String id) {

        BookIdRequest bookRequest = BookIdRequest.newBuilder().setId(id).build();

        Book bookResponse;

        try {
            bookResponse  = synchronousClient.getBook(bookRequest);
        } catch (StatusRuntimeException e) {
            Metadata metadata = Status.trailersFromThrowable(e);
            Metadata.Key<CustomError> errorKey = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
            CustomError customError = metadata != null ? metadata.get(errorKey) : null;
            throw new BookNotFoundException(
                    customError != null ? String.format("%s: %s", customError.getErrorType(), customError.getMessage())
                            : "Can't find book with this id");
        }

        return bookResponse.getAllFields();
    }

    public Map<Descriptors.FieldDescriptor, Object> createBook(BookModelDto bookModelDto) {

        CreateBookRequest bookRequest = CreateBookRequest.newBuilder().setBook(
                Book.newBuilder()
                .setTitle(bookModelDto.title())
                .setIsbn(bookModelDto.isbn())
                .setQuantity(bookModelDto.quantity())
                .setAuthor(bookModelDto.author())
                .build()
                )
                .build();

        Book bookResponse;

        try {
            bookResponse  = synchronousClient.createBook(bookRequest);
        } catch (StatusRuntimeException e) {
            Metadata metadata = Status.trailersFromThrowable(e);
            Metadata.Key<CustomError> errorKey = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
            CustomError customError = metadata != null ? metadata.get(errorKey) : null;
            throw new DuplicateUniqueFieldException(
                    customError != null ? String.format("%s: %s", customError.getErrorType(), customError.getMessage())
                            : "Duplicate unique fields");
        }

        return bookResponse.getAllFields();
    }

    public Map<Descriptors.FieldDescriptor, Object> updateBook(BookModelDto bookModelDto, String id) {

        UpdateBookRequest bookRequest = UpdateBookRequest.newBuilder().setBook(
                Book.newBuilder()
                .setId(id)
                .setTitle(bookModelDto.title())
                .setIsbn(bookModelDto.isbn())
                .setQuantity(bookModelDto.quantity())
                .setAuthor(bookModelDto.author())
                .build()
        )
                .build();

        Book bookResponse;

        try {
            bookResponse  = synchronousClient.updateBook(bookRequest);
        } catch (StatusRuntimeException e) {
            Metadata metadata = Status.trailersFromThrowable(e);
            Metadata.Key<CustomError> errorKey = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
            CustomError customError = metadata != null ? metadata.get(errorKey) : null;
            throw new BookNotFoundException(
                    customError != null ? String.format("%s: %s", customError.getErrorType(), customError.getMessage())
                            : "Can't find book with this id");
        }

        return bookResponse.getAllFields();
    }

    public Deleted.Status deleteBook(String id) {

        BookIdRequest bookRequest = BookIdRequest.newBuilder().setId(id).build();

        Deleted deleted;

        try {
            deleted  = synchronousClient.deleteBook(bookRequest);
        } catch (StatusRuntimeException e) {
            Metadata metadata = Status.trailersFromThrowable(e);
            Metadata.Key<CustomError> errorKey = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
            CustomError customError = metadata != null ? metadata.get(errorKey) : null;
            throw new BookNotFoundException(
                    customError != null ? String.format("%s: %s", customError.getErrorType(), customError.getMessage())
                            : "Can't find book with this id");
        }

        return deleted.getStatus();
    }
}
