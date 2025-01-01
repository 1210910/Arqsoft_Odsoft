package pt.psoft.g1.psoftg1.bookmanagement.services;


import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;

/**
 *
 */
public interface BookService {
    Book create(BookViewAMQP bookViewAMQP);
    Book update(BookViewAMQP bookViewAMQP);
    List<BookCountDTO> findTop5BooksLent();
}
