package pt.psoft.g1.psoftg1.authormanagement.repositories.mongodb;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.mongodb.AuthorMongoDB;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryMongoDB extends MongoRepository<AuthorMongoDB, Long> {
   // @Override
    Optional<AuthorMongoDB> findById(Long authorNumber);

    List<AuthorMongoDB> findByNameName(String name);


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

