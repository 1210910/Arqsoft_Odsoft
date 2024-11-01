package pt.psoft.g1.psoftg1.authormanagement.repositories.mongodb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.mongodb.AuthorMongoDB;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.mappers.AuthorMapperMongoDB;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.mongodb.LendingMongoDB;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.mappers.LendingMongoDBMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.mongodb.LendingMongoDBRepository;

import java.util.*;

@Profile("mongodb")
@Qualifier("mongoDbRepo")
@Component
public class AuthorRepositoryMongoDBImpl implements AuthorRepository {

    private final AuthorRepositoryMongoDB authorRepositoryMongoDB;

    private final AuthorMapperMongoDB authorMapperMongoDB;

    private final LendingMongoDBRepository lendingRepositoryMongoDB;

    private final LendingMongoDBMapper lendingMapperMongoDB;

    @Autowired
    @Lazy
    public AuthorRepositoryMongoDBImpl(AuthorRepositoryMongoDB authorRepositoryMongoDB, AuthorMapperMongoDB authorMapperMongoDB, LendingMongoDBRepository lendingRepositoryMongoDB, LendingMongoDBMapper lendingMapperMongoDB) {
        this.authorRepositoryMongoDB = authorRepositoryMongoDB;
        this.authorMapperMongoDB = authorMapperMongoDB;
        this.lendingRepositoryMongoDB = lendingRepositoryMongoDB;
        this.lendingMapperMongoDB = lendingMapperMongoDB;
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
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        // Fetch all authors with find all method
        List<AuthorMongoDB> authors = authorRepositoryMongoDB.findAll();
        System.out.println(authors);

        // Fetch all lendings with find all method
        List<LendingMongoDB> lendingsMongoDB = lendingRepositoryMongoDB.findAll();
        System.out.println(lendingsMongoDB);

        // Need to check lendings per author
        Map<String, Long> authorLendingCounts = new HashMap<>(); // Map to store author lending counts

        // Count lendings per author
        for (LendingMongoDB lendingMongoDB : lendingsMongoDB) {
            List<AuthorMongoDB> bookAuthors = lendingMongoDB.getBook().getAuthors();
            System.out.println("Lista dos autores do livro " + bookAuthors);
            for (AuthorMongoDB authorMongoDB : bookAuthors) {
                String authorId = authorMongoDB.getAuthorNumber();
                authorLendingCounts.put(authorId, authorLendingCounts.getOrDefault(authorId, 0L) + 1);
            }
        }

        // Create AuthorLendingView list
        List<AuthorLendingView> authorLendingViews = new ArrayList<>();
        for (AuthorMongoDB author : authors) {
            Long count = authorLendingCounts.get(author.getAuthorNumber());
            if (count != null) {
                authorLendingViews.add(new AuthorLendingView(author.getName(), count));
            }
        }
        return new PageImpl<>(authorLendingViews, pageableRules, authorLendingViews.size());
    }


//    @Override
//    public List<AuthorLendingView> findTopAuthorByLendings(Pageable pageable) {
//        // Step 1: Fetch all authors
//        List<AuthorMongoDB> authors = authorRepositoryMongoDB.findAll();
//
//        // Step 2: Fetch all lendings
//        List<LendingMongoDB> lendingsMongoDB = lendingRepositoryMongoDB.findAll(); // Assume you have a Lending repository
//        List<Lending> lendings = new ArrayList<>();
//
//        for (LendingMongoDB lendingMongoDB : lendingsMongoDB) {
//            lendings.add(lendingMapperMongoDB.toDomain(lendingMongoDB));
//        }
//
//        // Step 3: Count lendings per author
//        Map<String, Long> authorLendingCounts = new HashMap<>();
//
//        for (Lending lending : lendings) {
//            String authorId = lending.getBook().getAuthorId(); // Adjust based on how your lending references authors
//            authorLendingCounts.put(authorId, authorLendingCounts.getOrDefault(authorId, 0L) + 1);
//        }
//
//        // Step 4: Create AuthorLendingView list
//        List<AuthorLendingView> authorLendingViews = new ArrayList<>();
//        for (AuthorMongoDB author : authors) {
//            Long count = authorLendingCounts.get(author.getAuthorNumber());
//            if (count != null) {
//                authorLendingViews.add(new AuthorLendingView(author.getName().getFullName(), count));
//            }
//        }
//
//        // Step 5: Sort the authorLendingViews by lendingCount in descending order
//        authorLendingViews.sort((a1, a2) -> Long.compare(a2.getLendingCount(), a1.getLendingCount()));
//
//        // Step 6: Implement pagination if necessary
//        int start = Math.toIntExact(pageable.getOffset());
//        int end = Math.min(start + pageable.getPageSize(), authorLendingViews.size());
//        List<AuthorLendingView> paginatedList = authorLendingViews.subList(start, end);
//
//        // Step 7: Return the paginated result
//        return paginatedList;
//    }


    @Override
    public void delete(Author author) {

    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        return null;
    }
}
