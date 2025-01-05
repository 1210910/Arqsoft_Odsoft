package pt.psoft.g1.psoftg1.bookacquisitionmanagement.services;


import pt.psoft.g1.psoftg1.bookacquisitionmanagement.api.BookAcquisitionViewAMQP;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.BookAcquisition;

/**
 *
 */
public interface BookAcquisitionService {
    BookAcquisition create(CreateBookAcquisitionRequest request, String isbn);
    BookAcquisition create(BookAcquisitionViewAMQP bookAcquisitionViewAMQP);
    BookAcquisition save(BookAcquisition bookAcquisition);
//    BookAcquisition findByIsbn(String isbn);
//    BookAcquisition update(UpdateBookRequest request, String currentVersion);
    BookAcquisition update(BookAcquisitionViewAMQP bookViewAMQP);
//    List<BookAcquisition> findByGenre(String genre);
//    List<BookAcquisition> findByTitle(String title);
//    List<BookAcquisition> findByAuthorName(String authorName);
//    List<BookCountDTO> findTop5BooksLent();
//    BookAcquisition removeBookPhoto(String isbn, long desiredVersion);
//    List<BookAcquisition> getBooksSuggestionsForReader(String readerNumber);
//    List<BookAcquisition> searchBooks(Page page, SearchBooksQuery query);
}
