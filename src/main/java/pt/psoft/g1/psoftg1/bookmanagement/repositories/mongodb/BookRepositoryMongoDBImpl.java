package pt.psoft.g1.psoftg1.bookmanagement.repositories.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.repositories.mongodb.AuthorRepositoryMongoDB;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.mappers.BookMapperMongoDB;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.bookmanagement.model.mongodb.BookMongoDB;
import pt.psoft.g1.psoftg1.authormanagement.model.mongodb.AuthorMongoDB;
import pt.psoft.g1.psoftg1.genremanagement.model.mongodb.GenreMongoDB;
import pt.psoft.g1.psoftg1.genremanagement.repositories.mongodb.GenreRepositoryMongoDB;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Profile("mongodb")
@Qualifier("mongoDbRepo")
@Component
public class BookRepositoryMongoDBImpl implements BookRepository {

    private final BookRepositoryMongoDB bookRepositoryMongoDB;
    private final AuthorRepositoryMongoDB authorRepositoryMongoDB;
    private final GenreRepositoryMongoDB genreRepositoryMongoDB;
    private final BookMapperMongoDB bookMapperMongoDB;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    @Lazy
    public BookRepositoryMongoDBImpl(BookRepositoryMongoDB bookRepositoryMongoDB,
                                     AuthorRepositoryMongoDB authorRepositoryMongoDB,
                                     BookMapperMongoDB bookMapperMongoDB, GenreRepositoryMongoDB genreRepositoryMongoDB, MongoTemplate mongoTemplate) {
        this.bookRepositoryMongoDB = bookRepositoryMongoDB;
        this.authorRepositoryMongoDB = authorRepositoryMongoDB;
        this.bookMapperMongoDB = bookMapperMongoDB;
        this.genreRepositoryMongoDB = genreRepositoryMongoDB;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return bookRepositoryMongoDB.findByGenre(genre)
                .stream()
                .map(bookMapperMongoDB::toDomain)
                .toList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        System.out.println("I AM FINDING BOOKS BY TITLE MATE");
        return bookRepositoryMongoDB.findByTitle(title)
                .stream()
                .map(bookMapperMongoDB::toDomain)
                .toList();
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        return bookRepositoryMongoDB.findByAuthorName(authorName)
                .stream()
                .map(bookMapperMongoDB::toDomain)
                .toList();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        System.out.println("I AM FINDING BOOKS BY ISBN MATE");
        Optional<BookMongoDB> bookMongoDBFound = bookRepositoryMongoDB.findByIsbn(isbn);
        if (bookMongoDBFound.isEmpty()) {
            return Optional.empty();
        }
        System.out.println("BOOK MONGO DB FOUND: " + bookMongoDBFound.get().getTitle());
        System.out.println("BOOK MONGO DB GENRE: " + bookMongoDBFound.get().getGenre());
        Book bookDomainFound = bookMapperMongoDB.toDomain(bookMongoDBFound.get());
        System.out.println("BOOK PASSED BY THE MAPPER: " + bookDomainFound.getTitle());
        return Optional.of(bookDomainFound);
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        // Assuming you have a method to get the top 5 books lent in the MongoDB repository
        return bookRepositoryMongoDB.findTop5BooksLent(oneYearAgo, pageable);
    }

    @Override
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
        return bookRepositoryMongoDB.findBooksByAuthorNumber(authorNumber)
                .stream()
                .map(bookMapperMongoDB::toDomain)
                .toList();
    }

    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        Query mongoQuery = new Query();

        // Build query criteria similar to JPA's CriteriaQuery
        if (title != null && !title.isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("title").regex("^" + title, "i"));
        }

        if (genre != null && !genre.isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("genre").regex("^" + genre, "i"));
        }

        if (authorName != null && !authorName.isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("authors.name").regex("^" + authorName, "i"));
        }

        // Pagination
        Pageable pageable = PageRequest.of(page.getNumber() - 1, page.getLimit());
        mongoQuery.with(pageable);

        // Sort by title alphabetically
        mongoQuery.with(Sort.by(Sort.Direction.ASC, "title"));

        List<BookMongoDB> bookEntities = mongoTemplate.find(mongoQuery, BookMongoDB.class);

        // Map results to model (assuming you have a similar bookEntityMapper)
        List<Book> books = bookEntities.stream()
                .map(bookMapperMongoDB::toDomain)
                .toList();

        return books;
    }

    @Override
    public Book save(Book book) {
        BookMongoDB bookMongoDB = bookMapperMongoDB.toMongoDB(book);
        System.out.println("Book Genre: " + bookMongoDB.getGenre());
        System.out.println("I AM SAVING BOOKS MATE");

        List<AuthorMongoDB> authors = bookMongoDB.getAuthors().stream()
                .map(author -> {
                    AuthorMongoDB existingAuthor = authorRepositoryMongoDB.findByNameName(author.getName()).get(0);
                    if (existingAuthor == null) {
                        System.out.println("AUTHOR IS NULL");
                        existingAuthor = authorRepositoryMongoDB.save(author);
                    }
                    System.out.println("AUTHOR IS NOT NULL");
                    return existingAuthor;
                })
                .toList();

        // Check if the genre exists to assign it to the book
        if (bookMongoDB.getGenre() != null) {
            System.out.println("GENRE NOT NULL: " + bookMongoDB.getGenre());
            // Verifica se o gênero já existe no banco de dados pelo nome
            GenreMongoDB existingGenre = genreRepositoryMongoDB.findByGenre(bookMongoDB.getGenre().getGenre()).get();
            if (existingGenre == null) {
                System.out.println("GENRE IS NULL");
                // Se o gênero não existe, salva o novo gênero
                existingGenre = genreRepositoryMongoDB.save(bookMongoDB.getGenre());
                bookMongoDB.setGenre(existingGenre);
            }
            System.out.println("GENRE IS NOT NULL");
            bookMongoDB.setGenre(existingGenre); // Atualiza o gênero da BookEntity com o gênero persistido
        }

        System.out.println("AUTHORS: " + authors);
        System.out.println("GENRE: " + bookMongoDB.getGenre());
        bookMongoDB.setAuthors(authors);
        BookMongoDB savedEntity = bookRepositoryMongoDB.save(bookMongoDB);
        System.out.println("SAVED ENTITY: " + savedEntity.getGenre());
        return bookMapperMongoDB.toDomain(savedEntity);
    }

    @Override
    public void delete(Book book) {
        // Assuming you have a method to delete by ISBN or ID
        //bookRepositoryMongoDB.deleteByIsbn(book.getIsbn());
    }
}
