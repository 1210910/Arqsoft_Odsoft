//package pt.psoft.g1.psoftg1.integrationTests.bookmanagement.services.whitebox;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.web.multipart.MultipartFile;
//import pt.psoft.g1.psoftg1.authormanagement.model.Author;
//import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
//import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
//import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
//import pt.psoft.g1.psoftg1.bookmanagement.repositories.relational.BookRepositorySqlServer;
//import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
//import pt.psoft.g1.psoftg1.bookmanagement.services.BookServiceImpl;
//import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
//import pt.psoft.g1.psoftg1.bookmanagement.services.recomendationAlgs.RecomendationAlgorithm;
//import pt.psoft.g1.psoftg1.exceptions.ConflictException;
//import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
//import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
//import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
//import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
//import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
//import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class BookServiceWhiteBoxTest {
//
//    @Autowired
//    private BookRepository bookRepository;
//    @Autowired
//    @Lazy
//    private GenreRepository genreRepository;
//    @Autowired
//    @Lazy
//    private AuthorRepository authorRepository;
//    @Autowired
//    @Lazy
//    private PhotoRepository photoRepository;
//    @Autowired
//    @Lazy
//    private RecomendationAlgorithm recomendationAlgorithm;
//
//    @Autowired
//    @Lazy
//    private BookServiceImpl bookService;
//
//    private static Author author;
//
//    private static Genre genre;
//    @Autowired
//    private BookRepositorySqlServer bookRepositorySqlServer;
//
//    @BeforeEach
//    void setUp() {
//        genre = genreRepository.save(new Genre("Fantasia"));
//        author = authorRepository.save(new Author("Jack Doohan", "Jack Doohan is a writer", null, null));
//    }
//
//    @Test
//    void testCreate_BookAlreadyExists_ThrowsConflictException() {
//        String isbn = "9789720706386";
//        CreateBookRequest request = new CreateBookRequest();
//        request.setTitle("Invisible Book");
//        request.setGenre("Fantasia");
//        request.setAuthors(List.of(Long.parseLong(author.getAuthorNumber())));
//        request.setDescription("This is a book that you can't see");
//        request.setPhotoURI(null);
//        request.setPhoto(null);
//
//        Book existingBook = new Book(
//                isbn,
//                "Invisible Book",
//                "This is a book that you can't see",
//                genre,
//                List.of(author),
//                null
//        );
//        bookRepository.save(existingBook);
//
//        // Mock bookRepository to return a book for this ISBN
//        when(bookRepository.findByIsbn(isbn)).thenReturn((Optional.empty()));
//
//        bookService.create(request, isbn);
//
//        // Verify that findByIsbn was called once
//        verify(bookRepository).findByIsbn(isbn);
//    }
//
//    @Test
//    void testCreate_GenreNotFound_ThrowsNotFoundException() {
//        // Arrange
//        CreateBookRequest request = new CreateBookRequest();
//        request.setTitle("Invisible Book");
//        request.setGenre("Fantasia");
//        request.setAuthors(List.of(Long.parseLong(author.getAuthorNumber())));
//        request.setDescription("This is a book that you can't see");
//        request.setPhotoURI(null);
//        request.setPhoto(null);
//
//        String isbn = "9789720706386";
//
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
//        when(genreRepository.findByString(request.getGenre())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(NotFoundException.class, () -> bookService.create(request, isbn));
//
//        // Verify
//        verify(bookRepository).findByIsbn(isbn);
//        verify(genreRepository).findByString(request.getGenre());
//    }
//
//    @Test
//    void testCreate_CallsExpectedMethodsSuccessfully() {
//        // Arrange
//        CreateBookRequest request = new CreateBookRequest();
//        request.setTitle("Invisible Book");
//        request.setGenre("Fantasia");
//        request.setAuthors(List.of(Long.parseLong(author.getAuthorNumber())));
//        request.setDescription("This is a book that you can't see");
//        request.setPhotoURI(null);
//        request.setPhoto(null);
//
//        String isbn = "9789720706386";
//
//        Book createdBook = bookService.create(request, isbn);
//
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
//        when(genreRepository.findByString(request.getGenre())).thenReturn(Optional.of(genre));
//        when(authorRepository.findByAuthorNumber(any())).thenReturn(Optional.of(author));
//
//        // Act
//        bookService.create(request, isbn);
//
//        // Verify
//        verify(bookRepository).findByIsbn(isbn);
//        verify(genreRepository).findByString(request.getGenre());
//        verify(authorRepository).findByAuthorNumber(any());
//        verify(bookRepository).save(any(Book.class));
//    }
//}
