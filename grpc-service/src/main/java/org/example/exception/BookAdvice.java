package org.example.exception;

import com.example.bookstore.grpc.CustomError;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class BookAdvice {

    @GrpcExceptionHandler(BookNotFoundException.class)
    public StatusRuntimeException handleBookNotFoundException(BookNotFoundException exception) {
        Status status = Status.NOT_FOUND;
        Metadata metadata = new Metadata();
        Metadata.Key<CustomError> errorKey = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
        metadata.put(errorKey, CustomError.newBuilder()
                .setMessage(exception.getMessage())
                .setErrorType("NOT_FOUND")
                .build());
        return status.asRuntimeException(metadata);
    }

    @GrpcExceptionHandler(DuplicateUniqueFieldException.class)
    public StatusRuntimeException handleDuplicateUniqueFieldException(DuplicateUniqueFieldException exception) {
        Status status = Status.INVALID_ARGUMENT;
        Metadata metadata = new Metadata();
        Metadata.Key<CustomError> errorKey = ProtoUtils.keyForProto(CustomError.getDefaultInstance());
        metadata.put(errorKey, CustomError.newBuilder()
                .setMessage(exception.getMessage())
                .setErrorType("INVALID_ARGUMENT")
                .build());
        return status.asRuntimeException(metadata);
    }
}
