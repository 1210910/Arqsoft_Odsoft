package pt.psoft.g1.psoftg1.lms_book_command.producer.bookmanagement;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.api.*;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventPublisher;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {BookEventPublisher.class, BookViewAMQPMapperImpl.class},
        properties = {
                "stubrunner.amqp.mockConnection=true",
                "spring.profiles.active=test"
        }
)
@Provider("book_event-producer")
@PactFolder("target/pacts")
public class BookEventPublisherTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookEventPublisherTest.class);

    @Autowired
    private BookEventPublisher bookEventPublisher;

    @Autowired
    private BookViewAMQPMapperImpl bookViewAMQPMapper;

    @MockBean
    private RabbitTemplate template;

    @MockBean
    private DirectExchange direct;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
        if (!"a book lending request event".equals(interaction.getDescription())) {
            System.out.println("Entered here");
            context.verifyInteraction();
        } else {
            LOGGER.warn("Ignoring unrelated interaction: {}", interaction.getDescription());
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @PactVerifyProvider("a book created event")
    public MessageAndMetadata bookCreated() throws JsonProcessingException {
        LOGGER.info("Invoking bookCreated provider method");

        Author author = new Author("Pedro Miguel", "O melhor da cidade da Maia", null, "2");
        author.setAuthorNumber("3");
        Genre genre = new Genre("Informação");
        Book book = new Book("1439549719859", "Lewis Hamilton - GOAT of F1", "The biography of the absolute best of Formula 1", genre, List.of(author), "2");
        book.setVersion(1L);
        bookEventPublisher.sendBookCreated(book);
        System.out.println("Book created event sent");

        BookViewAMQP bookViewAMQP = new BookViewAMQP();
        bookViewAMQP.setTitle(book.getTitle().toString());
        bookViewAMQP.setAuthorIds(book.getAuthors().stream().map(Author::getAuthorNumber).collect(Collectors.toList()));
        bookViewAMQP.setIsbn(book.getIsbn());
        bookViewAMQP.setDescription(book.getDescription());
        bookViewAMQP.setGenre(book.getGenre().toString());
        bookViewAMQP.setVersion(book.getVersion().toString());

        Message<String> message = new BookMessageBuilder().withBook(bookViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    @PactVerifyProvider("a book updated event")
    public MessageAndMetadata bookUpdated() throws JsonProcessingException {
        LOGGER.info("Invoking bookUpdated provider method");

        Author author = new Author("Pedro Miguel", "O melhor da cidade da Maia", null, "2");
        author.setAuthorNumber("3");
        Genre genre = new Genre("Informação");
        Book book = new Book("1439549719859", "Lewis Hamilton - GOAT of F1", "The biography of the absolute best of Formula 1", genre, List.of(author), "2");
        book.setVersion(1L);
        bookEventPublisher.sendBookUpdated(book, 1L);

        BookViewAMQP bookViewAMQP = new BookViewAMQP();
        bookViewAMQP.setTitle(book.getTitle().toString());
        bookViewAMQP.setAuthorIds(book.getAuthors().stream().map(Author::getAuthorNumber).collect(Collectors.toList()));
        bookViewAMQP.setIsbn(book.getIsbn());
        bookViewAMQP.setDescription(book.getDescription());
        bookViewAMQP.setGenre(book.getGenre().toString());
        bookViewAMQP.setVersion(book.getVersion().toString());

        Message<String> message = new BookMessageBuilder().withBook(bookViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
        HashMap<String, Object> metadata = new HashMap<>();
        message.getHeaders().forEach((k, v) -> metadata.put(k, v));

        return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
    }
}