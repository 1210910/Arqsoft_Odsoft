package pt.psoft.g1.psoftg1.unitTests.authormanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorServiceImpl;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceImplTest {
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private PhotoRepository photoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByAuthorNumber() {
        String authorNumber = "AUTH123";
        Author author = mock(Author.class);
        when(authorRepository.findByAuthorNumber(authorNumber)).thenReturn(Optional.of(author));

        Optional<Author> result = authorService.findByAuthorNumber(authorNumber);

        assertTrue(result.isPresent());
        assertEquals(author, result.get());
        verify(authorRepository).findByAuthorNumber(authorNumber);
    }

    @Test
    void testFindByName() {
        String name = "AuthorName";
        List<Author> authors = List.of(mock(Author.class));
        when(authorRepository.searchByNameNameStartsWith(name)).thenReturn(authors);

        List<Author> result = authorService.findByName(name);

        assertEquals(authors, result);
        verify(authorRepository).searchByNameNameStartsWith(name);
    }

    @Test
    void testCreateAuthor() {
        CreateAuthorRequest request = mock(CreateAuthorRequest.class);
        Author author = mock(Author.class);
        when(authorMapper.create(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.create(request);

        assertEquals(author, result);
        verify(authorRepository).save(author);
        verify(authorMapper).create(request);
    }

    @Test
    void createAuthorWithNullPhoto() {
        CreateAuthorRequest request = mock(CreateAuthorRequest.class);
        when(request.getPhoto()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn("photo.jpg");
        Author author = mock(Author.class);
        when(authorMapper.create(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.create(request);

        assertEquals(author, result);
        verify(authorRepository).save(author);
        verify(authorMapper).create(request);
    }

    @Test
    void testPartialUpdateSuccess() {
        String authorNumber = "AUTH123";
        UpdateAuthorRequest updateRequest = mock(UpdateAuthorRequest.class);
        long desiredVersion = 1L;

        Author author = mock(Author.class);
        when(authorRepository.findByAuthorNumber(authorNumber)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.partialUpdate(authorNumber, updateRequest, desiredVersion);

        assertEquals(author, result);
        verify(author).applyPatch(desiredVersion, updateRequest);
        verify(authorRepository).save(author);
    }

    @Test
    void testPartialUpdateWithNullPhoto() {
        String authorNumber= "AUTH123";
        UpdateAuthorRequest updateRequest = mock(UpdateAuthorRequest.class);
        when(updateRequest.getPhoto()).thenReturn(null);
        when(updateRequest.getPhotoURI()).thenReturn("photo.jpg");
        long desiredVersion = 1L;

        Author author = mock(Author.class);
        when(authorRepository.findByAuthorNumber(authorNumber)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.partialUpdate(authorNumber, updateRequest, desiredVersion);
        verify(author).applyPatch(desiredVersion, updateRequest);
        verify(authorRepository).save(author);
    }

    @Test
    void testPartialUpdateAuthorNotFound() {
        String authorNumber = "AUTH123";
        UpdateAuthorRequest updateRequest = mock(UpdateAuthorRequest.class);
        long desiredVersion = 1L;

        when(authorRepository.findByAuthorNumber(authorNumber)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.partialUpdate(authorNumber, updateRequest, desiredVersion));
    }

    @Test
    void testFindTopAuthorByLendings() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<AuthorLendingView> authors = List.of(mock(AuthorLendingView.class));
        when(authorRepository.findTopAuthorByLendings(pageRequest)).thenReturn(new PageImpl<>(authors));

        List<AuthorLendingView> result = authorService.findTopAuthorByLendings();

        assertEquals(authors, result);
        verify(authorRepository).findTopAuthorByLendings(pageRequest);
    }

    @Test
    void testFindBooksByAuthorNumber() {
        String authorNumber = "AUTH123";
        List<Book> books = List.of(mock(Book.class));
        when(bookRepository.findBooksByAuthorNumber(authorNumber)).thenReturn(books);

        List<Book> result = authorService.findBooksByAuthorNumber(authorNumber);

        assertEquals(books, result);
        verify(bookRepository).findBooksByAuthorNumber(authorNumber);
    }

    @Test
    void testFindCoAuthorsByAuthorNumber() {
        String authorNumber = "AUTH123";
        List<Author> coAuthors = List.of(mock(Author.class));
        when(authorRepository.findCoAuthorsByAuthorNumber(authorNumber)).thenReturn(coAuthors);

        List<Author> result = authorService.findCoAuthorsByAuthorNumber(authorNumber);

        assertEquals(coAuthors, result);
        verify(authorRepository).findCoAuthorsByAuthorNumber(authorNumber);
    }

    @Test
    void testRemoveAuthorPhoto() {
        String authorNumber = "AUTH123";
        long desiredVersion = 1L;
        Author author = mock(Author.class);
        Photo photo = mock(Photo.class);
        when(authorRepository.findByAuthorNumber(authorNumber)).thenReturn(Optional.of(author));
        when(author.getPhoto()).thenReturn(photo);
        when(photo.getPhotoFile()).thenReturn("photo.jpg");
        when(authorRepository.save(author)).thenReturn(author);

        Optional<Author> result = authorService.removeAuthorPhoto(authorNumber, desiredVersion);

        assertTrue(result.isPresent());
        assertEquals(author, result.get());
        verify(author).removePhoto(desiredVersion);
        verify(photoRepository).deleteByPhotoFile("photo.jpg");
    }



    @Test
    void testRemoveAuthorPhotoNotFound() {
        String authorNumber = "AUTH123";
        long desiredVersion = 1L;

        when(authorRepository.findByAuthorNumber(authorNumber)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.removeAuthorPhoto(authorNumber, desiredVersion));
    }
}
