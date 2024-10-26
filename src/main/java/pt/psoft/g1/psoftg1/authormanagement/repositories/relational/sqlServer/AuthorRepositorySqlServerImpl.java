package pt.psoft.g1.psoftg1.authormanagement.repositories.relational.sqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.relational.AuthorEntity;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.mappers.AuthorEntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("sqlServer")
@Qualifier("authorSqlServerRepo")
@Component
public class AuthorRepositorySqlServerImpl implements AuthorRepository {

    private final AuthorRepositorySqlServer authorRepositorySqlServer;

    private final AuthorEntityMapper authorEntityMapper;

    @Autowired
    @Lazy
    public AuthorRepositorySqlServerImpl(AuthorRepositorySqlServer authorRepositorySqlServer, AuthorEntityMapper authorEntityMapper) {
        this.authorRepositorySqlServer = authorRepositorySqlServer;
        this.authorEntityMapper = authorEntityMapper;
    }
    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {

        if (authorRepositorySqlServer.findByAuthorNumber(authorNumber).isEmpty()) {
            return Optional.empty();
        }else {
            Author author = authorEntityMapper.toDomain(authorRepositorySqlServer.findByAuthorNumber(authorNumber).get());
            return Optional.of(author);
        }

    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        List<Author> authors = new ArrayList<>();

        authorRepositorySqlServer.searchByNameNameStartsWith(name).forEach(authorEntity -> {
            authors.add(authorEntityMapper.toDomain(authorEntity));
        });


        return authors;
    }

    @Override
    public List<Author> searchByNameName(String name) {

        List<Author> authors = new ArrayList<>();

        authorRepositorySqlServer.searchByNameName(name).forEach(authorEntity -> {
            authors.add(authorEntityMapper.toDomain(authorEntity));
        });


        return authors;
    }

    @Override
    public Author save(Author author) {


        return authorEntityMapper.toDomain( authorRepositorySqlServer.save(authorEntityMapper.toEntity(author)));
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        System.out.println(authorRepositorySqlServer.findTopAuthorByLendings(pageableRules));
        return authorRepositorySqlServer.findTopAuthorByLendings(pageableRules);
    }

    @Override
    public void delete(Author author) {
        authorRepositorySqlServer.delete(authorEntityMapper.toEntity(author));
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        List<Author> authors = new ArrayList<>();

        authorRepositorySqlServer.findCoAuthorsByAuthorNumber(authorNumber).forEach(authorEntity -> {
            authors.add(authorEntityMapper.toDomain(authorEntity));
        });


        return authors;
    }
}
