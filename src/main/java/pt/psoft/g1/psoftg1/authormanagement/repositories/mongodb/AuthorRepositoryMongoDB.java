package pt.psoft.g1.psoftg1.authormanagement.repositories.mongodb;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
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

   //@Query("SELECT new pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView(a.name.name, COUNT(l.pk)) " +
   //        "FROM Book b " +
   //        "JOIN b.authors a " +
   //        "JOIN LendingEntity l ON l.book.pk = b.pk " +
   //        "GROUP BY a.name " +
   //        "ORDER BY COUNT(l) DESC")
   //Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable);

    //@Query("SELECT DISTINCT coAuthor FROM Book b " +
    //        "JOIN b.authors coAuthor " +
    //        "WHERE b IN (SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber) " +
    //        "AND coAuthor.authorNumber <> :authorNumber")
    //List<AuthorMongoDB> findCoAuthorsById(Long authorNumber);
}

