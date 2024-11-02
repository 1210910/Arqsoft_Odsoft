package pt.psoft.g1.psoftg1.bookmanagement.services.recomendationAlgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@Profile("recomendationAlgorithm1")
@PropertySource({"classpath:config/library.properties"})
@Component
public class RecomendationAlgorithm1 implements RecomendationAlgorithm{

    @Value("${maxRecommendations}")
    private int MAX_RECOMMENDATIONS;

    @Value("${maxGenres}")
    private int MAX_GENRES;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Autowired
    @Lazy
    public RecomendationAlgorithm1(BookRepository bookRepository,GenreRepository genreRepository) {

        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    public List<Book> recommend(String readerNumber) {

        List<String> genres = genreRepository.getMostLentGenres(MAX_GENRES);
        List<Book> recommendedBooks = new ArrayList<>();

        for (String genre : genres) {
            System.out.println("Genre: " + genre);
            List<Book> books = bookRepository.findMostLentBooksByGenre(MAX_RECOMMENDATIONS,genre);
            recommendedBooks.addAll(books);
        }

        System.out.println("Recommended books: " + recommendedBooks.size());

        return recommendedBooks;
    }
}
