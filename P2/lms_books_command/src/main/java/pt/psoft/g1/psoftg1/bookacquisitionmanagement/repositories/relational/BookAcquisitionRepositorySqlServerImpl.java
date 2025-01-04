package pt.psoft.g1.psoftg1.bookacquisitionmanagement.repositories.relational;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.relational.AuthorEntity;
import pt.psoft.g1.psoftg1.authormanagement.repositories.relational.sqlServer.AuthorRepositorySqlServer;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.BookAcquisition;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.relational.BookAcquisitionEntity;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.repositories.BookAcquisitionRepository;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.repositories.mappers.BookAcquisitionEntityMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.relational.GenreEntity;
import pt.psoft.g1.psoftg1.genremanagement.repositories.relational.GenreRepositorySqlServer;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("sqlServer")
@Qualifier("bookAcquisitionSqlServerRepo")
@Component
public class BookAcquisitionRepositorySqlServerImpl implements BookAcquisitionRepository {

    private final BookAcquisitionRepositorySqlServer bookAcquisitionRepositorySqlServer;
    private final BookAcquisitionEntityMapper bookAcquisitionEntityMapper;

    private final GenreRepositorySqlServer genreRepository;
    private final AuthorRepositorySqlServer authorRepository;
    private final EntityManager em;

    @Autowired
    @Lazy
    public BookAcquisitionRepositorySqlServerImpl(BookAcquisitionRepositorySqlServer bookAcquisitionRepositorySqlServer, BookAcquisitionEntityMapper bookAcquisitionEntityMapper,
                                       GenreRepositorySqlServer genreRepository,
                                       AuthorRepositorySqlServer authorRepository, EntityManager em) {
        this.bookAcquisitionRepositorySqlServer = bookAcquisitionRepositorySqlServer;
        this.bookAcquisitionEntityMapper = bookAcquisitionEntityMapper;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.em = em;
    }


//    @Override
//    public List<Book> findByGenre(String genre) {
//        List<Book> book = new ArrayList<>();
//        for (BookEntity b: bookRepositorySqlServer.findByGenre(genre)) {
//            book.add(bookEntityMapper.toModel(b));
//        }
//        return book;
//    }
//
//    @Override
//    public List<Book> findByTitle(String title) {
//        List<Book> book = new ArrayList<>();
//        for (BookEntity b: bookRepositorySqlServer.findByTitle(title)) {
//            book.add(bookEntityMapper.toModel(b));
//        }
//        return book;
//    }
//
//    @Override
//    public List<Book> findByAuthorName(String authorName) {
//        List<Book> book = new ArrayList<>();
//        List<BookEntity> books=bookRepositorySqlServer.findByAuthorName(authorName);
//        //System.out.println(book);
//        for (BookEntity b: books) {
//            book.add(bookEntityMapper.toModel(b));
//        }
//        return book;
//    }
//
    @Override
    public Optional<BookAcquisition> findByIsbn(String isbn) {
        if (bookAcquisitionRepositorySqlServer.findByIsbn(isbn).isEmpty()) {
            return Optional.empty();
        }else{
            BookAcquisitionEntity bookAcquisition = bookAcquisitionRepositorySqlServer.findByIsbn(isbn).get();

            return Optional.of(bookAcquisitionEntityMapper.toModel(bookAcquisition)) ;
        }

    }
//
//    @Override
//    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
//        return null;
//    }
//
//
//    @Override
//    public List<Book> findBooksByAuthorNumber(String authorNumber) {
//        List<Book> books = new ArrayList<>();
//        // Put the string authorNumber to long
//         for (BookEntity bookEntity : bookRepositorySqlServer.findBooksByAuthorNumber(Long.parseLong(authorNumber))) {
//             books.add(bookEntityMapper.toModel(bookEntity));
//         }
//            return books;
//
//    }

    @Override
    public BookAcquisition save(BookAcquisition bookAcquisition) {

        System.out.println("Book Acquisition pk has to be null here: " + bookAcquisition.getPk());

        BookAcquisitionEntity bookAcquisitionEntity = bookAcquisitionEntityMapper.toEntity(bookAcquisition);

        List<AuthorEntity> authors = new ArrayList<>(); // Lista para autores que serão associados ao livro

        for (AuthorEntity author : bookAcquisitionEntity.getAuthors()) {
            // Verifica se o autor já existe no banco de dados pelo nome
            AuthorEntity existingAuthor = authorRepository.searchByNameName(author.getName()).get(0);
            if (existingAuthor == null) {
                // Se o autor não existe, salva o novo autor
                existingAuthor = authorRepository.save(author);
            }
            authors.add(existingAuthor); // Adiciona o autor à lista de autores do livro
        }

        if (bookAcquisitionEntity.getGenre() != null) {
            // Verifica se o gênero já existe no banco de dados pelo nome
            GenreEntity existingGenre = genreRepository.findByString(bookAcquisitionEntity.getGenre().getGenre()).get();
            if (existingGenre == null) {
                // Se o gênero não existe, salva o novo gênero
                existingGenre = genreRepository.save(bookAcquisitionEntity.getGenre());
                bookAcquisitionEntity.setGenre(existingGenre); // Atualiza o gênero da BookEntity com o gênero persistido
            }
            bookAcquisitionEntity.setGenre(existingGenre); // Atualiza o gênero da BookEntity com o gênero persistido
        }

        // Atualiza a lista de autores da BookEntity com os autores persistidos
        bookAcquisitionEntity.setAuthors(authors);

        BookAcquisitionEntity savedEntity = bookAcquisitionRepositorySqlServer.save(bookAcquisitionEntity);

        System.out.println("Book Acquisition pk cannot be null here: " + savedEntity.getPk());

        return bookAcquisitionEntityMapper.toModel(savedEntity);
    }

//    @Override
//    public void delete(Book book) {
//
//    }
//
//    @Override
//    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query)
//    {
//        String title = query.getTitle();
//        String genre = query.getGenre();
//        String authorName = query.getAuthorName();
//
//        final CriteriaBuilder cb = em.getCriteriaBuilder();
//        final CriteriaQuery<BookEntity> cq = cb.createQuery(BookEntity.class);
//        final Root<BookEntity> root = cq.from(BookEntity.class);
//        final Join<BookEntity, Genre> genreJoin = root.join("genre");
//        final Join<BookEntity, Author> authorJoin = root.join("authors");
//        cq.select(root);
//
//        final List<Predicate> where = new ArrayList<>();
//
//        if (StringUtils.hasText(title))
//            where.add(cb.like(root.get("title").get("title"), title + "%"));
//
//        if (StringUtils.hasText(genre))
//            where.add(cb.like(genreJoin.get("genre"), genre + "%"));
//
//        if (StringUtils.hasText(authorName))
//            where.add(cb.like(authorJoin.get("name").get("name"), authorName + "%"));
//
//        cq.where(where.toArray(new Predicate[0]));
//        cq.orderBy(cb.asc(root.get("title"))); // Order by title, alphabetically
//
//        final TypedQuery<BookEntity> q = em.createQuery(cq);
//        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
//        q.setMaxResults(page.getLimit());
//
//        List <Book> books = new ArrayList<>();
//
//        for (BookEntity bookEntity : q.getResultList()) {
//            books.add(bookEntityMapper.toModel(bookEntity));
//        }
//
//        return books;
//    }
//
//    @Override
//    public List<Book> findMostLentBooksByGenre(int maxBooks, String genre) {
//        /*
//        // Criação do CriteriaBuilder e CriteriaQuery
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
//
//        // Definição do Root para a entidade Lending
//        Root<LendingEntity> lendingRoot = cq.from(LendingEntity.class);
//
//        // Definição dos joins para acessar Book e Genre
//        Join<LendingEntity, BookEntity> bookJoin = lendingRoot.join("book");
//        Join<BookEntity, GenreEntity> genreJoin = bookJoin.join("genre");
//
//        // Filtragem pelo gênero especificado
//        cq.where(cb.equal(genreJoin.get("genre"), genre));
//
//        // Contagem de ocorrências de empréstimos por livro
//        Expression<Long> lendingCount = cb.count(lendingRoot);
//
//        // Seleciona o livro e sua contagem de empréstimos
//        cq.multiselect(bookJoin, lendingCount);
//
//        // Agrupa pela entidade Book para contar cada livro individualmente
//        cq.groupBy(bookJoin);
//
//        // Ordena pela contagem de empréstimos em ordem decrescente
//        cq.orderBy(cb.desc(lendingCount));
//
//        // Criação e execução da consulta
//        TypedQuery<Tuple> query = em.createQuery(cq);
//        query.setMaxResults(maxBooks);
//
//        // Processa os resultados e retorna uma lista de livros
//        List<Tuple> results = query.getResultList();
//        List<BookEntity> mostLentBooks = new ArrayList<>();
//
//        for (Tuple result : results) {
//            mostLentBooks.add(result.get(0, BookEntity.class));
//        }
//
//        List<Book> mostLentBooksModel = new ArrayList<>();
//
//        for (BookEntity bookEntity : mostLentBooks) {
//            mostLentBooksModel.add(bookEntityMapper.toModel(bookEntity));
//        }
//
//        return mostLentBooksModel;
//
//         */
//        return null;
//    }


}
