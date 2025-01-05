package pt.psoft.g1.psoftg1.CDCTests.producer.bookacquisitionmanagement;

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
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.api.*;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.BookAcquisition;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.publishers.BookAcquisitionEventPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;


import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {BookAcquisitionEventPublisher.class, BookAcquisitionViewAMQPMapperImpl.class},
        properties = {
                "stubrunner.amqp.mockConnection=true",
                "spring.profiles.active=test"
        }
)
@Provider("bookAcquisition_event-producer")
@PactFolder("target/pacts")
public class BookAcquisitionEventPublisherTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookAcquisitionEventPublisherTest.class);

    @Autowired
    private BookAcquisitionEventPublisher bookAcquisitionEventPublisher;

    @Autowired
    private BookAcquisitionViewAMQPMapperImpl bookAcquisitionViewAMQPMapper;

    @MockBean
    private RabbitTemplate template;

    @MockBean
    private DirectExchange direct;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context){
        context.setTarget(new MessageTestTarget());
    }

    @PactVerifyProvider("a book acquisition created event")
    public MessageAndMetadata bookAcquisitionCreated() throws JsonProcessingException {
        LOGGER.info("Invoking book acquisition created provider method");

        Author author = new Author(
                "Pedro Miguel",
                "O melhor da cidade da Maia",
                null,
                "2");
        author.setAuthorNumber("3");
        Genre genre = new Genre("Informação");
        BookAcquisition bookAcquisition = new BookAcquisition(
                "1439549719859",
                "Lewis Hamilton - GOAT of F1",
                "The biography of the absolute best of Formula 1",
                genre,
                List.of(author),
                "2");
        bookAcquisition.setVersion(1L);
        bookAcquisitionEventPublisher.sendBookAcquisitionCreated(bookAcquisition);
        System.out.println("Book Acquisition created event sent");

        BookAcquisitionViewAMQP bookAcquisitionViewAMQP = new BookAcquisitionViewAMQP();
        bookAcquisitionViewAMQP.setTitle(bookAcquisition.getTitle().toString());
        bookAcquisitionViewAMQP.setAuthorIds(bookAcquisition.getAuthors().stream().map(Author::getAuthorNumber).collect(Collectors.toList()));
        bookAcquisitionViewAMQP.setAcqID(bookAcquisition.getAcqID());
        bookAcquisitionViewAMQP.setDescription(bookAcquisition.getDescription());
        bookAcquisitionViewAMQP.setGenre(bookAcquisition.getGenre().toString());
        bookAcquisitionViewAMQP.setVersion(bookAcquisition.getVersion().toString());

        Message<String> message = new BookAcquisitionMessageBuilder().withBookAcquisition(bookAcquisitionViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    @PactVerifyProvider("a book acquisition updated event")
    public MessageAndMetadata bookAcquisitionUpdated() throws JsonProcessingException {
        LOGGER.info("Invoking book acquisition updated provider method");

        Author author = new Author(
                "Pedro Miguel",
                "O melhor da cidade da Maia",
                null,
                "2");
        author.setAuthorNumber("3");
        Genre genre = new Genre("Informação");
        BookAcquisition bookAcquisition = new BookAcquisition(
                "1439549719859",
                "Lewis Hamilton - GOAT of F1",
                "The biography of the absolute best of Formula 1",
                genre,
                List.of(author),
                "2");
        bookAcquisition.setVersion(1L);
        bookAcquisitionEventPublisher.sendBookAcquisitionUpdated(bookAcquisition, bookAcquisition.getVersion());
        System.out.println("Book Acquisition updated event sent");

        BookAcquisitionViewAMQP bookAcquisitionViewAMQP = new BookAcquisitionViewAMQP();
        bookAcquisitionViewAMQP.setTitle(bookAcquisition.getTitle().toString());
        bookAcquisitionViewAMQP.setAuthorIds(bookAcquisition.getAuthors().stream().map(Author::getAuthorNumber).
                collect(Collectors.toList()));
        bookAcquisitionViewAMQP.setAcqID(bookAcquisition.getAcqID());
        bookAcquisitionViewAMQP.setDescription(bookAcquisition.getDescription());
        bookAcquisitionViewAMQP.setGenre(bookAcquisition.getGenre().toString());
        bookAcquisitionViewAMQP.setVersion(bookAcquisition.getVersion().toString());

        Message<String> message = new BookAcquisitionMessageBuilder().withBookAcquisition(bookAcquisitionViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
        HashMap<String, Object> metadata = new HashMap<>();
        message.getHeaders().forEach((k, v) -> metadata.put(k, v));

        return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
    }
}
