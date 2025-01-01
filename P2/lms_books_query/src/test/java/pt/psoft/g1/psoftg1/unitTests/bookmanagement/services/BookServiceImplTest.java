//package pt.psoft.g1.psoftg1.unitTests.bookmanagement.services;
//
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.core.parameters.P;
//import org.springframework.security.oauth2.server.resource.introspection.SpringReactiveOpaqueTokenIntrospector;
//import org.springframework.web.multipart.MultipartFile;
//import pt.psoft.g1.psoftg1.authormanagement.model.Author;
//import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
//import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
//import pt.psoft.g1.psoftg1.bookmanagement.services.*;
//import pt.psoft.g1.psoftg1.bookmanagement.services.recomendationAlgs.RecomendationAlgorithm;
//import pt.psoft.g1.psoftg1.exceptions.ConflictException;
//import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
//import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
//import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
//import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
//import pt.psoft.g1.psoftg1.shared.model.Photo;
//import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
//import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
//import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
//import pt.psoft.g1.psoftg1.shared.services.Page;
//
//
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class BookServiceImplTest {
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private GenreRepository genreRepository;
//
//    @Mock
//    private PhotoRepository photoRepository;
//
//    @Mock
//    private AuthorRepository authorRepository;
//
//    @Mock
//    private ReaderRepository readerRepository;
//
//    @Mock
//    private RecomendationAlgorithm recomendationAlgorithm;
//
//    @InjectMocks
//    private BookServiceImpl bookService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetBooksSuggestionsForReader_Success() {
//        // Arrange
//        String readerNumber = "reader123";
//        Book book1 = mock(Book.class);
//        when(book1.getTitle()).thenReturn(mock(Title.class));
//        when(book1.getTitle().getTitle()).thenReturn("Book Title 1");
//        Book book2 = mock(Book.class);
//        when(book2.getTitle()).thenReturn(mock(Title.class));
//        when(book2.getTitle().getTitle()).thenReturn("Book Title 2");
//        List<Book> expectedBooks = List.of(book1, book2);
//
//        // Mock recommendation algorithm
//        when(recomendationAlgorithm.recommend(readerNumber)).thenReturn(expectedBooks);
//
//        // Act
//        List<Book> result = bookService.getBooksSuggestionsForReader(readerNumber);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals("Book Title 1", result.get(0).getTitle().getTitle());
//        verify(recomendationAlgorithm, times(1)).recommend(readerNumber);
//    }
//
//    @Test
//    void testGetBooksSuggestionsForReader_ReaderNotFound() {
//        // Arrange
//        String readerNumber = "unknownReader";
//
//        // Mock behavior of the reader repository to throw NotFoundException
//        when(readerRepository.findByReaderNumber(readerNumber)).thenReturn(Optional.empty());
//        when(recomendationAlgorithm.recommend(readerNumber)).thenThrow(new NotFoundException("Reader not found with provided login"));
//
//        // Act & Assert
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
//            bookService.getBooksSuggestionsForReader(readerNumber);
//        });
//
//        assertEquals("Reader not found with provided login", exception.getMessage());
//        verify(recomendationAlgorithm, times(1)).recommend(anyString());
//    }
//
//    @Test
//    void testSearchBooks_WithNullPageAndQuery() {
//        // Arrange
//        SearchBooksQuery query = mock(SearchBooksQuery.class);
//        when(query.getTitle()).thenReturn("Book Title 1");
//        when(query.getAuthorName()).thenReturn("Author Name 1");
//        when(query.getGenre()).thenReturn("Fiction");
//        Book book1 = mock(Book.class);
//        when(book1.getTitle()).thenReturn(mock(Title.class));
//        when(book1.getTitle().getTitle()).thenReturn("Book Title 1");
//        List<Book> expectedBooks = List.of(book1);
//
//        when(bookRepository.searchBooks(any(Page.class), eq(query))).thenReturn(expectedBooks);
//
//        // Act
//        List<Book> result = bookService.searchBooks(null, query);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Book Title 1", result.get(0).getTitle().getTitle());
//        verify(bookRepository, times(1)).searchBooks(any(Page.class), eq(query));
//    }
//
//    @Test
//    void testSearchBooks_WithDefaultPage() {
//        // Arrange
//        SearchBooksQuery query = mock(SearchBooksQuery.class);
//        Book book1 = mock(Book.class);
//        when(book1.getTitle()).thenReturn(mock(Title.class));
//        when(book1.getTitle().getTitle()).thenReturn("Book Title 1");
//        List<Book> expectedBooks = List.of(book1);
//        Page page = mock(Page.class);
//        when(page.getNumber()).thenReturn(1);
//        when(page.getLimit()).thenReturn(10);
//
//        when(bookRepository.searchBooks(page, query)).thenReturn(expectedBooks);
//
//        when(bookRepository.searchBooks(any(Page.class), eq(query))).thenReturn(expectedBooks);
//
//        // Act
//        List<Book> result = bookService.searchBooks(page, query);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Book Title 1", result.get(0).getTitle().getTitle());
//        verify(bookRepository, times(1)).searchBooks(any(Page.class), eq(query));
//    }
//
//    @Test
//    void testFindTop5BooksLent_Success() {
//        // Arrange
//        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
//        Book book1 = mock(Book.class);
//        Book book2 = mock(Book.class);
//        Book book3 = mock(Book.class);
//        Book book4 = mock(Book.class);
//        Book book5 = mock(Book.class);
//
//        BookCountDTO bookCountDTO1 = mock(BookCountDTO.class);
//        BookCountDTO bookCountDTO2 = mock(BookCountDTO.class);
//        BookCountDTO bookCountDTO3 = mock(BookCountDTO.class);
//        BookCountDTO bookCountDTO4 = mock(BookCountDTO.class);
//        BookCountDTO bookCountDTO5 = mock(BookCountDTO.class);
//
//        when(bookCountDTO1.getBook()).thenReturn(book1);
//        when(bookCountDTO2.getBook()).thenReturn(book2);
//        when(bookCountDTO3.getBook()).thenReturn(book3);
//        when(bookCountDTO4.getBook()).thenReturn(book4);
//        when(bookCountDTO5.getBook()).thenReturn(book5);
//
//        when(bookCountDTO1.getLendingCount()).thenReturn(10L);
//        when(bookCountDTO2.getLendingCount()).thenReturn(9L);
//        when(bookCountDTO3.getLendingCount()).thenReturn(8L);
//        when(bookCountDTO4.getLendingCount()).thenReturn(7L);
//        when(bookCountDTO5.getLendingCount()).thenReturn(6L);
//
//
//        List<BookCountDTO> expectedTopBooks = List.of(bookCountDTO1, bookCountDTO2, bookCountDTO3, bookCountDTO4, bookCountDTO5);
//
//        // Mocking the repository to return a paginated response with the top 5 books
//        when(bookRepository.findTop5BooksLent(oneYearAgo, PageRequest.of(0, 5)))
//                .thenReturn(new PageImpl<>(expectedTopBooks, PageRequest.of(0, 5), expectedTopBooks.size()));
//
//        // Act
//        List<BookCountDTO> result = bookService.findTop5BooksLent();
//
//        // Assert
//        assertNotNull(result);
//        verify(bookRepository, times(1)).findTop5BooksLent(oneYearAgo, PageRequest.of(0, 5));
//    }
//
//    @Test
//    void testUpdateBook_Success() {
//        String isbn = "1234567890";
//        String currentVersion = "1";
//        Author author = mock(Author.class);
//        List<String> authors = List.of("1");
//        UpdateBookRequest request = mock(UpdateBookRequest.class);
//        when(request.getGenre()).thenReturn("NonExistentGenre");
//        when(request.getTitle()).thenReturn("Book Title");
//        when(request.getDescription()).thenReturn("Book Description");
//        when(request.getAuthors()).thenReturn(authors);
//        when(request.getGenre()).thenReturn("Fiction");
//        when(request.getIsbn()).thenReturn(isbn);
//
//        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(author));
//
//        Genre genre = mock(Genre.class);
//        when(genre.getGenre()).thenReturn("Fiction");
//        when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(genre));
//
//        Book book = mock(Book.class);
//        when(book.getTitle()).thenReturn(mock(Title.class));
//        when(book.getTitle().getTitle()).thenReturn("Book Title");
//        when(book.getDescription()).thenReturn("Book Description");
//        when(book.getGenre()).thenReturn(genre);
//        when(book.getAuthors()).thenReturn(List.of(author));
//
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
//        when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(genre));
//
//        bookService.update(request, currentVersion);
//
//        verify(bookRepository, times(1)).save(book);
//    }
//
//    @Test
//    void testUpdateBook_GenreNotFound() {
//        String isbn = "1234567890";
//        String currentVersion = "1";
//        Author author = mock(Author.class);
//        List<String> authors = List.of("1");
//        UpdateBookRequest request = mock(UpdateBookRequest.class);
//        when(request.getGenre()).thenReturn("NonExistentGenre");
//        when(request.getTitle()).thenReturn("Book Title");
//        when(request.getDescription()).thenReturn("Book Description");
//        when(request.getAuthors()).thenReturn(authors);
//        when(request.getGenre()).thenReturn("Fiction");
//        when(request.getIsbn()).thenReturn(isbn);
//
//        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(author));
//
//        Genre genre = mock(Genre.class);
//        when(genre.getGenre()).thenReturn("Fiction");
//        when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(genre));
//
//        Book book = mock(Book.class);
//        when(book.getTitle()).thenReturn(mock(Title.class));
//        when(book.getTitle().getTitle()).thenReturn("Book Title");
//        when(book.getDescription()).thenReturn("Book Description");
//        when(book.getGenre()).thenReturn(genre);
//        when(book.getAuthors()).thenReturn(List.of(author));
//
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
//        when(genreRepository.findByString(request.getGenre())).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> bookService.update(request, currentVersion));
//        verify(bookRepository, never()).save(any(Book.class));
//    }
//
//    @Test
//    void testCreateBook_Success() {
//        String isbn = "9789720706386";
//        Author author = mock(Author.class);
//        List<Long> authors = List.of(1L);
//        CreateBookRequest request = mock(CreateBookRequest.class);
//        when(request.getGenre()).thenReturn("NonExistentGenre");
//        when(request.getTitle()).thenReturn("Book Title");
//        when(request.getDescription()).thenReturn("Book Description");
//        when(request.getAuthors()).thenReturn(authors);
//        when(request.getGenre()).thenReturn("Fiction");
//
//        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(author));
//
//        Genre genre = mock(Genre.class);
//        when(genre.getGenre()).thenReturn("Fiction");
//        when(genreRepository.findByString("Fiction")).thenReturn(Optional.of(genre));
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
//
//        Book book = mock(Book.class);
//        when(book.getTitle()).thenReturn(mock(Title.class));
//        when(book.getTitle().getTitle()).thenReturn("Book Title");
//        when(book.getDescription()).thenReturn("Book Description");
//        when(book.getGenre()).thenReturn(genre);
//        when(book.getAuthors()).thenReturn(List.of(author));
//        when(bookRepository.save(any(Book.class))).thenReturn(book);
//
//        Book createdBook = bookService.create(request, isbn);
//
//        assertNotNull(createdBook);
//        assertEquals("Book Title", createdBook.getTitle().getTitle());
//        verify(bookRepository, times(1)).save(any(Book.class));
//    }
//
//    @Test
//    void testCreateBook_BookAlreadyExists() {
//        String isbn = "9789720706386";
//        CreateBookRequest request = new CreateBookRequest();
//        request.setTitle("Book Title");
//        request.setDescription("Book Description");
//        request.setGenre("Fiction");
//
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(mock(Book.class)));
//
//        assertThrows(ConflictException.class, () -> bookService.create(request, isbn));
//        verify(bookRepository, never()).save(any(Book.class));
//    }
//
//    @Test
//    void testCreateBook_GenreNotFound() {
//        String isbn = "9789720706386";
//        Author author = mock(Author.class);
//        List<Long> authors = List.of(1L);
//        CreateBookRequest request = mock(CreateBookRequest.class);
//        when(request.getGenre()).thenReturn("NonExistentGenre");
//        when(request.getTitle()).thenReturn("Book Title");
//        when(request.getDescription()).thenReturn("Book Description");
//        when(request.getAuthors()).thenReturn(authors);
//
//        when(authorRepository.findByAuthorNumber("1")).thenReturn(Optional.of(author));
//
//
//        when(genreRepository.findByString("NonExistentGenre")).thenReturn(Optional.empty());
//        assertThrows(NotFoundException.class, () -> bookService.create(request, isbn));
//        verify(bookRepository, never()).save(any(Book.class));
//    }
//
//    @Test
//    void testRemoveBookPhoto_Success() {
//        String isbn = "9789720706386";
//        Book book2 = mock(Book.class);
//        Book book = mock(Book.class);
//        Photo photo = mock(Photo.class);
//        when(book.getPhoto()).thenReturn(photo);
//        when(book.getVersion()).thenReturn(1L);
//        when(book.getPhoto().getPhotoFile()).thenReturn("photo.jpg");
//
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
//        when(bookRepository.save(any(Book.class))).thenReturn(book2);
//
//        Book updatedBook = bookService.removeBookPhoto(isbn, book.getVersion());
//
//        assertNull(updatedBook.getPhoto());
//        verify(photoRepository, times(1)).deleteByPhotoFile("photo.jpg");
//    }
//
//    @Test
//    void testRemoveBookPhoto_BookNotFound() {
//        String isbn = "nonexistentISBN";
//
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> bookService.removeBookPhoto(isbn, 1L));
//        verify(photoRepository, never()).deleteByPhotoFile(anyString());
//    }
//
//    @Test
//    void testFindByGenre_Success() {
//        String genreName = "Fiction";
//        List<Book> books = new ArrayList<>();
//        Book book = mock(Book.class);
//        when(book.getTitle()).thenReturn(mock(Title.class));
//        when(book.getTitle().getTitle()).thenReturn("Title");
//        books.add(book);
//
//
//
//        when(bookRepository.findByGenre(genreName)).thenReturn(books);
//
//        List<Book> foundBooks = bookService.findByGenre(genreName);
//
//        assertNotNull(foundBooks);
//        assertEquals(1, foundBooks.size());
//        assertEquals("Title", foundBooks.get(0).getTitle().getTitle());
//    }
//
//    @Test
//    void testFindByIsbn_BookExists() {
//        String isbn = "9789720706386";
//        Book book = mock(Book.class);
//        when(book.getIsbn()).thenReturn(isbn);
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
//
//        Book foundBook = bookService.findByIsbn(isbn);
//
//        assertNotNull(foundBook);
//        assertEquals(isbn, foundBook.getIsbn());
//    }
//
//    @Test
//    void testFindByIsbn_BookNotFound() {
//        String isbn = "nonexistentISBN";
//
//        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> bookService.findByIsbn(isbn));
//    }
//}