package org.example.repository;

import org.example.model.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookModel, UUID> {
    Optional<BookModel> findBookModelByIsbn(String isbn);
}
