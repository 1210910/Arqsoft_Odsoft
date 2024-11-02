package pt.psoft.g1.psoftg1.unitTests.bookmanagement.model;

import com.google.rpc.context.AttributeContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class BookTest {

    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";
    private final Author mockAuthor = Mockito.mock(Author.class);
    private final Author mockAuthor2 = Mockito.mock(Author.class);
    private final Genre mockGenre = Mockito.mock(Genre.class);
    private ArrayList<Author> authors = new ArrayList<>();

    @BeforeEach
    void setUp(){
        authors.clear();
    }

    @Test
    void ensureIsbnNotNull(){

        // Arrange
        authors.add(mockAuthor);

        // Act
        assertThrows(IllegalArgumentException.class, () -> new Book(null, validTitle, null, mockGenre, authors, null));
    }

    @Test
    void ensureTitleNotNull(){
        // Arrange
        authors.add(mockAuthor);

        // Act
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, null, null, mockGenre, authors, null));
    }

    @Test
    void ensureGenreNotNull(){
        // Arrange
        authors.add(mockAuthor);

        // Act
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null,null, authors, null));
    }

    @Test
    void ensureAuthorsNotNull(){

        // Arrange
        authors.add(mockAuthor);

        // Act
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null, mockGenre, null, null));
    }

    @Test
    void ensureAuthorsNotEmpty(){
        // Act
        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, validTitle, null, mockGenre, authors, null));
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() {

        // Arrange
        authors.add(mockAuthor);
        authors.add(mockAuthor2);

        // Act
        assertDoesNotThrow(() -> new Book(validIsbn, validTitle, null, mockGenre, authors, null));
    }

}