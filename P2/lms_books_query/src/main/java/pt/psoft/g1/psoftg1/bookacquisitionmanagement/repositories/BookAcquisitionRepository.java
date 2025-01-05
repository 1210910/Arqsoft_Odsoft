package pt.psoft.g1.psoftg1.bookacquisitionmanagement.repositories;

import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.BookAcquisition;

import java.util.Optional;


/**
 *
 */
public interface BookAcquisitionRepository {


//    List<Book> findByGenre(@Param("genre") String genre);
//    List<Book> findByTitle(@Param("title") String title);
//    List<Book> findByAuthorName(@Param("authorName") String authorName);
    Optional<BookAcquisition> findByAcqID(@Param("acqID") String isbn);
//    Page<BookCountDTO> findTop5BooksLent(@Param("oneYearAgo") LocalDate oneYearAgo, Pageable pageable);
//    List<Book> findBooksByAuthorNumber(String authorNumber);
//
//    List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query);
//
//    List<Book> findMostLentBooksByGenre(int maxBooks, String genre);

    BookAcquisition save(BookAcquisition book);
//    void delete(Book book);
}
