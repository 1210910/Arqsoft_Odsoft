package pt.psoft.g1.psoftg1.bookmanagement.services.recomendationAlgs;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.time.LocalDate;
import java.util.List;
@Profile("recomendationAlgorithm2")
@Component
@PropertySource({"classpath:config/library.properties"})
public class RecomendationAlgorithm2 implements RecomendationAlgorithm{

    @Value("${maxRecommendations}")
    private int MAX_RECOMMENDATIONS;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final ReaderRepository readerRepository;

    @Autowired
    @Lazy
    public RecomendationAlgorithm2(GenreRepository genreRepository, BookRepository bookRepository, ReaderRepository readerRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    @Override
    public List<Book> recommend(String readerNumber) {
        // Get the reader's age
        int readerAge = LocalDate.now().getYear() - readerRepository.findByReaderNumber(readerNumber).get().getBirthDate().getBirthDate().getYear();

        if (readerAge < 10) {
            return bookRepository.findMostLentBooksByGenre(MAX_RECOMMENDATIONS, "Infantil");
        } else if (readerAge < 18) {
            return bookRepository.findMostLentBooksByGenre(MAX_RECOMMENDATIONS, "Adulto");
        } else {
            System.out.println("Idoso");
            String genre = genreRepository.getMostLentGenreByReader(readerNumber);
            System.out.println(genre);
            List<Book> books = bookRepository.findMostLentBooksByGenre(MAX_RECOMMENDATIONS, genre);
            System.out.println(books);
            return books;
        }
    }
}
