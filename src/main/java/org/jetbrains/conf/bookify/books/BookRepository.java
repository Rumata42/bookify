package org.jetbrains.conf.bookify.books;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

interface BookRepository extends CrudRepository<Book, UUID> {
    @Query("SELECT book.id AS id, book.isbn AS isbn, book.name AS name, book.available AS available FROM book WHERE book.name LIKE UPPER(:name)")
    List<Book> findByNameContainingIgnoreCase(String name);
}

@Configuration
class BookCallbackConfiguration {
    @Bean
    BeforeConvertCallback<Book> beforeConvertCallback() {
        return (book) -> {
            if (book.isNew() && book.getId() == null) {
                book.setId(UUID.randomUUID());
            }
            return book;
        };
    }
}