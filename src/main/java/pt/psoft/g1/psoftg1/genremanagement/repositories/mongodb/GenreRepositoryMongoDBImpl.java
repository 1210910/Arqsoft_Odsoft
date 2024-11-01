package pt.psoft.g1.psoftg1.genremanagement.repositories.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.mongodb.GenreMongoDB;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.repositories.mappers.GenreMapperMongoDB;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Profile("mongodb")
@Qualifier("mongoDbRepo")
@Component
public class GenreRepositoryMongoDBImpl implements GenreRepository {

    private final GenreRepositoryMongoDB genreRepositoryMongoDB;

    private final GenreMapperMongoDB genreMapperMongoDB;

    private final MongoClient mongoClient;
    private final MongoClient mongo;

    @Autowired
    @Lazy
    public GenreRepositoryMongoDBImpl(GenreRepositoryMongoDB genreRepositoryMongoDB, GenreMapperMongoDB genreMapperMongoDB, MongoClient mongoClient, @Qualifier("mongo") MongoClient mongo) {
        this.genreRepositoryMongoDB = genreRepositoryMongoDB;
        this.genreMapperMongoDB = genreMapperMongoDB;
        this.mongoClient = mongoClient;
        this.mongo = mongo;
    }

    @Override
    public Iterable<Genre> findAll() {
        return null;
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        Optional<GenreMongoDB> genreMongoDBOptional = genreRepositoryMongoDB.findByGenre(genreName);
        System.out.println("GenreMongoDBOptional: " + genreMongoDBOptional);
        if (genreMongoDBOptional.isPresent()) {
            Genre genre = genreMapperMongoDB.toDomain(genreMongoDBOptional.get());
            return Optional.of(genre);
        }
        return Optional.empty();
    }

    @Override
    public Genre save(Genre genre) {
        GenreMongoDB mongoGenre = genreMapperMongoDB.toMongoDB(genre);
        System.out.println("MongoGenre: " + mongoGenre.getGenre());
        GenreMongoDB savedGenre = genreRepositoryMongoDB.save(mongoGenre);
        return genreMapperMongoDB.toDomain(savedGenre);
    }

    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        return null;
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        return List.of();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        return List.of();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public void delete(Genre genre) {

    }
}
