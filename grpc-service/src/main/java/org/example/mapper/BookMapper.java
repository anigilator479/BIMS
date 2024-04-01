package org.example.mapper;

import com.example.bookstore.grpc.Book;
import org.example.model.BookModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "quantity", source = "quantity")
    BookModel toModel(Book book);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "quantity", source = "quantity")
    BookModel toModelWithId(Book book);
}

