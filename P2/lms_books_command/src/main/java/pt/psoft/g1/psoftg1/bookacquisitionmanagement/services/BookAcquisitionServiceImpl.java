package pt.psoft.g1.psoftg1.bookacquisitionmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.api.BookAcquisitionViewAMQP;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.BookAcquisition;
//import pt.psoft.g1.psoftg1.bookacquisition.publishers.BookEventPublisher;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.publishers.BookAcquisitionEventPublisher;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.repositories.BookAcquisitionRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class BookAcquisitionServiceImpl implements BookAcquisitionService {

	private final BookAcquisitionRepository bookAcquisitionRepository;
	private final GenreRepository genreRepository;
	private final AuthorRepository authorRepository;
	private final PhotoRepository photoRepository;
	@Value("${suggestionsLimitPerGenre}")
	private long suggestionsLimitPerGenre;
	private final BookAcquisitionEventPublisher bookAcquisitionEventPublisher;

	@Override
	public BookAcquisition create(CreateBookAcquisitionRequest request, String isbn) {

		System.out.println("Entered on the create method inside the service class");

		final String title = request.getTitle();
		final String description = request.getDescription();
		final String photoURI = request.getPhotoURI();
		final String genre = request.getGenre();
		final List<String> authorIds = request.getAuthors();

		BookAcquisition savedBookAcquisition = create(isbn, title, description, photoURI, genre, authorIds);

		System.out.println("Saved book acquisition pk: " + savedBookAcquisition.getPk());

		System.out.println("Saved book acquisition title: " + savedBookAcquisition.getTitle());

		// Need to update the AMQP message to create on the different services

		if(savedBookAcquisition != null ) {
			bookAcquisitionEventPublisher.sendBookAcquisitionCreated(savedBookAcquisition);
		}

		return savedBookAcquisition;
	}

	@Override
	public BookAcquisition create(BookAcquisitionViewAMQP bookAcquisitionViewAMQP) {

		final String isbn = bookAcquisitionViewAMQP.getIsbn();
		final String description = bookAcquisitionViewAMQP.getDescription();
		final String title = bookAcquisitionViewAMQP.getTitle();
		final String photoURI = null;
		final String genre = bookAcquisitionViewAMQP.getGenre();
		final List<String> authorIds = bookAcquisitionViewAMQP.getAuthorIds();

		BookAcquisition bookAcquisition = create(isbn, title, description, photoURI, genre, authorIds);

		return bookAcquisition;
	}

	private BookAcquisition create(
						String isbn,
						String title,
                        String description,
                        String photoURI,
                        String genreName,
                        List<String> authorIds) {

		if (bookAcquisitionRepository.findByIsbn(isbn).isPresent()) {
			throw new ConflictException("Book Acquisition with acqId " + isbn + " already exists");
		}

		List<Author> authors = getAuthors(authorIds);

		final Genre genre = genreRepository.findByString(String.valueOf(genreName))
				.orElseThrow(() -> new NotFoundException("Genre not found"));

		BookAcquisition newBookAcquisition = new BookAcquisition(isbn, title, description, genre, authors, photoURI);

		return bookAcquisitionRepository.save(newBookAcquisition);
	}

	private List<Author> getAuthors(List<String> authorNumbers) {

		List<Author> authors = new ArrayList<>();
		for (String authorNumber : authorNumbers) {

			Optional<Author> temp = authorRepository.findByAuthorNumber(authorNumber);
			if (temp.isEmpty()) {
				continue;
			}

			Author author = temp.get();
			authors.add(author);
		}

		return authors;
	}


//	@Override
//	public BookAcquisition update(UpdateBookRequest request, String currentVersion) {
//
//		var book = findByIsbn(request.getIsbn());
//
//		List<String> authorsId = request.getAuthors();
//
//		MultipartFile photo = request.getPhoto();
//		String photoURI = request.getPhotoURI();
//		if (photo == null && photoURI != null || photo != null && photoURI == null) {
//			photoURI = null;
//		}
//
//		String genreId = request.getGenre();
//		String title = request.getTitle();
//		String description = request.getDescription();
//
//		Book updatedBook = update( book, currentVersion, title, description, photoURI, genreId, authorsId);
//		if( updatedBook!=null ) {
//			bookEventsPublisher.sendBookUpdated(updatedBook, Long.parseLong(currentVersion));
//		}
//
//		return updatedBook;
//	}

//	@Override
//	public BookAcquisition update(BookViewAMQP bookViewAMQP) {
//
//		final String version = bookViewAMQP.getVersion();
//		final String isbn = bookViewAMQP.getIsbn();
//		final String description = bookViewAMQP.getDescription();
//		final String title = bookViewAMQP.getTitle();
//		final String photoURI = null;
//		final String genre = bookViewAMQP.getGenre();
//		final List<String> authorIds = bookViewAMQP.getAuthorIds();
//
//		var book = findByIsbn(isbn);
//
//		Book bookUpdated = update(book, version, title, description, photoURI, genre, authorIds);
//
//		return bookUpdated;
//	}

//	private BookAcquisition update(Book book,
//                        String currentVersion,
//                        String title,
//                        String description,
//                        String photoURI,
//                        String genreId,
//                        List<String> authorsId) {
//
//		Genre genreObj = null;
//		if (genreId != null) {
//			Optional<Genre> genre = genreRepository.findByString(genreId);
//			if (genre.isEmpty()) {
//				throw new NotFoundException("Genre not found");
//			}
//			genreObj = genre.get();
//		}
//
//		List<Author> authors = new ArrayList<>();
//		if (authorsId != null) {
//			for (String authorNumber : authorsId) {
//				Optional<Author> temp = authorRepository.findByAuthorNumber(authorNumber);
//				if (temp.isEmpty()) {
//					continue;
//				}
//				Author author = temp.get();
//				authors.add(author);
//			}
//		}
//		else
//			authors = null;
//
//		book.applyPatch(Long.parseLong(currentVersion), title, description, photoURI, genreObj, authors);
//
//		return bookRepository.save(book);
//	}

	@Override
	public BookAcquisition save(BookAcquisition bookAcquisition) {
		return this.bookAcquisitionRepository.save(bookAcquisition);
	}

	@Override
	public BookAcquisition update(BookAcquisitionViewAMQP bookViewAMQP) {
		return null;
	}

//	@Override
//	public List<BookCountDTO> findTop5BooksLent(){
//		LocalDate oneYearAgo = LocalDate.now().minusYears(1);
//		Pageable pageableRules = PageRequest.of(0,5);
//		return this.bookRepository.findTop5BooksLent(oneYearAgo, pageableRules).getContent();
//	}
//
//	@Override
//	public Book removeBookPhoto(String isbn, long desiredVersion) {
//		Book book = this.findByIsbn(isbn);
//		String photoFile;
//		try {
//			photoFile = book.getPhoto().getPhotoFile();
//		}catch (NullPointerException e){
//			throw new NotFoundException("Book did not have a photo assigned to it.");
//		}
//
//		book.removePhoto(desiredVersion);
//		var updatedBook = bookRepository.save(book);
//		photoRepository.deleteByPhotoFile(photoFile);
//		return updatedBook;
//	}
//
//	@Override
//	public List<Book> findByGenre(String genre) {
//		return this.bookRepository.findByGenre(genre);
//	}
//
//	public List<Book> findByTitle(String title) {
//		return bookRepository.findByTitle(title);
//	}
//
//	@Override
//	public List<Book> findByAuthorName(String authorName) {
//		return bookRepository.findByAuthorName(authorName);
//	}
//
//	public Book findByIsbn(String isbn) {
//		return this.bookRepository.findByIsbn(isbn)
//				.orElseThrow(() -> new NotFoundException(Book.class, isbn));
//	}
//
//	public List<Book> getBooksSuggestionsForReader(String readerNumber) {
//		List<Book> books = new ArrayList<>();
//
//		//ReaderDetails readerDetails = readerRepository.findByReaderNumber(readerNumber)
//		//		.orElseThrow(() -> new NotFoundException("Reader not found with provided login"));
//	   	//
//		//List<Genre> interestList = readerDetails.getInterestList();
//		//
//		//if(interestList.isEmpty()) {
//		//	throw new NotFoundException("Reader has no interests");
//		//}
//		//
//		//for(Genre genre : interestList) {
//		//	List<Book> tempBooks = bookRepository.findByGenre(genre.toString());
//		//	if(tempBooks.isEmpty()) {
//		//		continue;
//		//	}
//		//
//		//	long genreBookCount = 0;
//		//
//        //    for (Book loopBook : tempBooks) {
//        //        if (genreBookCount >= suggestionsLimitPerGenre) {
//        //            break;
//        //        }
//		//
//        //        books.add(loopBook);
//		//		genreBookCount++;
//        //    }
//		//}
//
//		//books = recomendationAlgorithm.recommend(readerNumber);
//		//
//		//return books;
//		return null;
//	}
//
//	@Override
//	public List<Book> searchBooks(Page page, SearchBooksQuery query) {
//		if (page == null) {
//			page = new Page(1, 10);
//		}
//		if (query == null) {
//			query = new SearchBooksQuery("", "", "");
//		}
//		return bookRepository.searchBooks(page, query);
//	}
}
