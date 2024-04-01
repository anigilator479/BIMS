package service;

import com.example.bookstore.grpc.Book;
import com.example.bookstore.grpc.BookIdRequest;
import com.example.bookstore.grpc.CreateBookRequest;
import io.grpc.stub.StreamObserver;
import org.example.exception.BookNotFoundException;
import org.example.mapper.BookMapper;
import org.example.mapper.impl.BookMapperImpl;
import org.example.model.BookModel;
import org.example.repository.BookRepository;
import org.example.service.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private StreamObserver<Book> responseObserver;

    @InjectMocks
    private BookServiceImpl bookService;

    @Spy
    private BookMapper bookMapper = new BookMapperImpl();

    @Test
    @DisplayName("Verify the book returns when book exists")
    public void getBookById_WithValidId_ShouldReturnValidBook() {

        String bookId = "2834ea71-b413-4d7c-b3e0-76e6ebf6b205";

        BookIdRequest bookIdRequest = BookIdRequest.newBuilder().setId(bookId).build();

        BookModel book = new BookModel();
        book.setId(UUID.fromString(bookId));
        book.setAuthor("Senior Tomato");
        book.setTitle("How to cook");
        book.setIsbn("123456789");
        book.setQuantity(10);

        Book response = Book.newBuilder()
                .setId(book.getId().toString())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setQuantity(book.getQuantity())
                .setTitle(book.getTitle())
                .build();

        Mockito.when(bookRepository.findById(UUID.fromString(bookId))).thenReturn(Optional.of(book));

        bookService.getBook(bookIdRequest, responseObserver);

        Mockito.verify(responseObserver).onNext(response);
    }

    @Test
    @DisplayName("Verify the correct exception message returns with not valid id")
    public void getBookById_WithNotValidId_ShouldReturnException() {
        String bookId = "2834ea71-b413-4d7c-b3e0-76e6ebf6b205";

        BookIdRequest bookIdRequest = BookIdRequest.newBuilder().setId(bookId).build();

        Exception exception = Assertions.assertThrows(BookNotFoundException.class,
                () -> bookService.getBook(bookIdRequest, responseObserver));

        String expected = "Can't find book with id: " + bookId;

        String actual = exception.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify the correct bookDto returns after book was saved")
    public void createBook_Valid_ShouldReturnBook() {
        String bookId = "2834ea71-b413-4d7c-b3e0-76e6ebf6b205";

        BookModel book = new BookModel();
        book.setId(UUID.fromString(bookId));
        book.setAuthor("Senior Tomato");
        book.setTitle("How to cook");
        book.setIsbn("123456789");
        book.setQuantity(10);

        Mockito.when(bookRepository.save(Mockito.any(BookModel.class))).thenReturn(book);

        CreateBookRequest createBookRequest = CreateBookRequest.newBuilder().setBook(Book.newBuilder()
                .setId(bookId)
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setQuantity(book.getQuantity())
                .setTitle(book.getTitle())
                .build()
        ).build();

        bookService.createBook(createBookRequest, responseObserver);

        Mockito.verify(responseObserver).onNext(createBookRequest.getBook());
    }
}