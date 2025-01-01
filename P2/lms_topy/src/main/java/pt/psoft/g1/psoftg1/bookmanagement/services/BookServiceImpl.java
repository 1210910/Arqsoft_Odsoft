package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.relational.AuthorEntity;

import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.*;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.genremanagement.model.relational.GenreEntity;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final GenreRepository genreRepository;
	private final AuthorRepository authorRepository;
	private final PhotoRepository photoRepository;
	private final ReaderRepository readerRepository;

	@Value("${suggestionsLimitPerGenre}")
	private long suggestionsLimitPerGenre;



	@Override
	public Book create(BookViewAMQP bookViewAMQP) {

		final String isbn = bookViewAMQP.getIsbn();
		final String description = bookViewAMQP.getDescription();
		final String title = bookViewAMQP.getTitle();
		final String photoURI = null;
		final String genre = bookViewAMQP.getGenre();
		final List<Long> authorIds = bookViewAMQP.getAuthorIds();

		Book bookCreated = create(isbn, title, description, photoURI, genre, authorIds);

		return bookCreated;
	}

	private Book create( String isbn,
						 String title,
						 String description,
						 String photoURI,
						 String genreName,
						 List<Long> authorIds) {

		if (bookRepository.findByIsbn(isbn).isPresent()) {
			throw new ConflictException("Book with ISBN " + isbn + " already exists");
		}

		List<Author> authors = getAuthors(authorIds);

		final Genre genre = genreRepository.findByString(String.valueOf(genreName))
				.orElseThrow(() -> new NotFoundException("Genre not found"));

		Book newBook = new Book(isbn, title, description, genre, authors, photoURI);

		Book savedBook = bookRepository.save(newBook);

		return savedBook;
	}

	private List<Author> getAuthors(List<Long> authorNumbers) {

		List<Author> authors = new ArrayList<>();
		for (Long authorNumber : authorNumbers) {

			Optional<Author> temp = authorRepository.findByAuthorNumber(authorNumber.toString());
			if (temp.isEmpty()) {
				continue;
			}

			Author author = temp.get();
			authors.add(author);
		}

		return authors;
	}


	@Override
	public Book update(BookViewAMQP bookViewAMQP){
		return null;
	}

	@Override
	public List<BookCountDTO> findTop5BooksLent(){
		LocalDate oneYearAgo = LocalDate.now().minusYears(1);
		Pageable pageableRules = PageRequest.of(0,5);
		return this.bookRepository.findTop5BooksLent(oneYearAgo, pageableRules).getContent();
	}
}
