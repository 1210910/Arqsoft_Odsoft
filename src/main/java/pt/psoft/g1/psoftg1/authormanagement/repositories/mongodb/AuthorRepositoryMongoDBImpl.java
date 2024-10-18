package pt.psoft.g1.psoftg1.authormanagement.repositories.mongodb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
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
    public Optional<Author> findByAuthorNumber(Long authorNumber) {

        Author author = authorMapperMongoDB.toDomain(authorRepositoryMongoDB.findById(authorNumber).get());

        return Optional.of(author);
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        return null;
    }

    @Override
    public List<Author> searchByNameName(String name) {

        List<Author> authors =  new ArrayList<>();

        authorRepositoryMongoDB.findByNameName(name).forEach(authorMongoDB -> authors.add(authorMapperMongoDB.toDomain(authorMongoDB)));

        return authors;


    }

    @Override
    public Author save(Author author) {

        return authorMapperMongoDB.toDomain(authorRepositoryMongoDB.save(authorMapperMongoDB.toMongoDB(author)));

    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        return null;
    }

    @Override
    public void delete(Author author) {

    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        return null;
    }
}
