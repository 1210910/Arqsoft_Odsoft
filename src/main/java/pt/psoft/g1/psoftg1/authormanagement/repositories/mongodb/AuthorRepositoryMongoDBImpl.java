package pt.psoft.g1.psoftg1.authormanagement.repositories.mongodb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.mongodb.AuthorMongoDB;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.mappers.AuthorMapperMongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("mongodb")
@Qualifier("mongoDbRepo")
@Component
public class AuthorRepositoryMongoDBImpl implements AuthorRepository {

    private final AuthorRepositoryMongoDB authorRepositoryMongoDB;

    private final AuthorMapperMongoDB authorMapperMongoDB;



    @Autowired
    @Lazy
    public AuthorRepositoryMongoDBImpl(AuthorRepositoryMongoDB authorRepositoryMongoDB, AuthorMapperMongoDB authorMapperMongoDB) {
        this.authorRepositoryMongoDB = authorRepositoryMongoDB;
        this.authorMapperMongoDB = authorMapperMongoDB;
    }


    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        // Find the author by ID in MongoDB
        Optional<AuthorMongoDB> authorMongoDBOptional = authorRepositoryMongoDB.findById(authorNumber);

        // If the author is found, map it to the domain object and return
        if (authorMongoDBOptional.isPresent()) {
            Author author = authorMapperMongoDB.toDomain(authorMongoDBOptional.get());
            return Optional.of(author);
        }

        // Return an empty Optional if the author is not found
        return Optional.empty();
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {

        // Create a list to hold the authors that match the search criteria
        List<Author> authors = new ArrayList<>();

        // Use the repository to find authors with names starting with the provided string
        List<AuthorMongoDB> authorsMongoDBList = authorRepositoryMongoDB.findByNameName(name);
        System.out.println("List of authorsMongoDBList: " + authorsMongoDBList);

        // Map the MongoDB authors to domain and add them to the list
        authorsMongoDBList.forEach(authorMongoDB ->
                authors.add(authorMapperMongoDB.toDomain(authorMongoDB))
        );

        return authors;
    }

    @Override
    public List<Author> searchByNameName(String name) {

        List<Author> authors =  new ArrayList<>();

        authorRepositoryMongoDB.findByNameName(name).forEach(
                authorMongoDB -> authors.add(authorMapperMongoDB.toDomain(authorMongoDB)));

        return authors;
    }

    @Override
    public Author save(Author author) {

        // Convert domain model to MongoDB model
        System.out.println("Start logs");
        System.out.println(author.getAuthorNumber());
        AuthorMongoDB mongoAuthor = authorMapperMongoDB.toMongoDB(author);

        System.out.println(mongoAuthor.getAuthorNumber());

        // Save the MongoDB model to the repository
        AuthorMongoDB savedMongoAuthor = authorRepositoryMongoDB.save(mongoAuthor);

        System.out.println(savedMongoAuthor.getAuthorNumber());

        // Convert back to domain model and return
        return authorMapperMongoDB.toDomain(savedMongoAuthor);
    }


    @Override
    public void delete(Author author) {

    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        return null;
    }
}
