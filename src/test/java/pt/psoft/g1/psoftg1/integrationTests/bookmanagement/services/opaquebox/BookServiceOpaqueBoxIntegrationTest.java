package pt.psoft.g1.psoftg1.integrationTests.bookmanagement.services.opaquebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookServiceImpl;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class BookServiceOpaqueBoxIntegrationTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

//    @Autowired
//    private ReaderRepository readerRepository;

    private static Author author;

    private static Genre genre;

    //private static Reader reader;

    //private static ReaderDetails readerDetails;

    @BeforeEach
    void setUp() {
        genre = genreRepository.save(new Genre("Fantasia"));
        //Genre genre2 = genreRepository.save(new Genre("Setup"));
        author = authorRepository.save(new Author("Jack Doohan", "Jack Doohan is a writer", null, null));
        //reader = new Reader("Lewis Hamilton", "Xuba438976!");
        //List<Genre> interestList = List.of(genre, genre2);
        //readerDetails = new ReaderDetails(123213, reader, "2003-10-16", "913456781", true, true, true, null, interestList);
        //readerRepository.save(readerDetails);
    }

    @Test
    void createBook_andFindByIsbn() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Invisible Book");
        request.setGenre("Fantasia");
        request.setAuthors(List.of(Long.parseLong(author.getAuthorNumber())));
        request.setDescription("This is a book that you can't see");
        request.setPhotoURI(null);
        request.setPhoto(null);

        String isbn = "9789720706386";

        // Opaque-box: Create a book and retrieve it by ISBN
        Book createdBook = bookService.create(request, isbn);
        Book foundBook = bookService.findByIsbn(createdBook.getIsbn());

        // Assert that the returned book matches the input, without inspecting internal logic
        assertThat(foundBook.getIsbn()).isEqualTo(isbn);
        assertThat(foundBook.getTitle().getTitle()).isEqualTo("Invisible Book");
        assertThat(foundBook.getGenre().getGenre()).isEqualTo("Fantasia");
    }

    @Test
    void findByIsbn_notFound() {
        // Opaque-box: Simply validate output, expecting NotFoundException if ISBN does not exist
        assertThrows(NotFoundException.class, () -> bookService.findByIsbn("000000000"));
    }

    @Test
    void findByGenre() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Invisible Book");
        request.setGenre("Fantasia");
        request.setAuthors(List.of(Long.parseLong(author.getAuthorNumber())));
        request.setDescription("This is a book that you can't see");
        request.setPhotoURI(null);
        request.setPhoto(null);

        String isbn = "9789720706386";

        // Opaque-box: Create a book and retrieve it by ISBN
        Book createdBook = bookService.create(request, isbn);

        assertThat(bookService.findByGenre(createdBook.getGenre().getGenre())).isNotEmpty();
    }

    @Test
    void findByAuthorName(){
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Invisible Book");
        request.setGenre("Fantasia");
        request.setAuthors(List.of(Long.parseLong(author.getAuthorNumber())));
        request.setDescription("This is a book that you can't see");
        request.setPhotoURI(null);
        request.setPhoto(null);

        String isbn = "9789720706386";

        // Opaque-box: Create a book and retrieve it by ISBN
        Book createdBook = bookService.create(request, isbn);

        assertThat(bookService.findByAuthorName(createdBook.getAuthors().get(0).getName())).isNotEmpty();
    }

    @Test
    void findByTitle() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Invisible Book");
        request.setGenre("Fantasia");
        request.setAuthors(List.of(Long.parseLong(author.getAuthorNumber())));
        request.setDescription("This is a book that you can't see");
        request.setPhotoURI(null);
        request.setPhoto(null);

        String isbn = "9789720706386";

        // Opaque-box: Create a book and retrieve it by ISBN
        Book createdBook = bookService.create(request, isbn);

        assertThat(bookService.findByTitle(createdBook.getTitle().getTitle())).isNotEmpty();
    }

    @Test
    void findByTop5BooksLent() {
        //assertThat(bookService.findTop5BooksLent()).isNotEmpty();
    }
}
