package pt.psoft.g1.psoftg1.authormanagement.repositories.mongodb;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pt.psoft.g1.psoftg1.authormanagement.model.mongodb.AuthorMongoDB;

import java.util.List;
import java.util.Optional;

@Profile("mongodb")
public interface AuthorRepositoryMongoDB extends MongoRepository<AuthorMongoDB, String> {
    // @Override
    //Optional<AuthorMongoDB> findById(String authorNumber);
    @Query("{ 'name.name': {$regex : ?0 }}")  // Query to check if the string name is included in the name of author
    List<AuthorMongoDB> findByNameName(String name);

    Optional<AuthorMongoDB> findByAuthorNumber(String authorNumber);

    List<AuthorMongoDB> findAll();

}

