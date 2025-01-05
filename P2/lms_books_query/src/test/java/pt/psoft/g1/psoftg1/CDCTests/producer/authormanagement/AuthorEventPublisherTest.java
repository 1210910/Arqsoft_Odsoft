package pt.psoft.g1.psoftg1.lms_book_command.producer.authormanagement;

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
import pt.psoft.g1.psoftg1.authormanagement.api.*;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.publisher.AuthorEventPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lms_book_command.producer.bookmanagement.BookEventPublisherTest;
import pt.psoft.g1.psoftg1.lms_book_command.producer.bookmanagement.BookMessageBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {AuthorEventPublisher.class, AuthorViewAMQPMapperImpl.class},
        properties = {
                "stubrunner.amqp.mockConnection=true",
                "spring.profiles.active=test"
        }
)
@Provider("author_event-producer")
@PactFolder("target/pacts")
public class AuthorEventPublisherTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorEventPublisherTest.class);

    @Autowired
    private AuthorEventPublisher authorEventPublisher;

    @Autowired
    private AuthorViewAMQPMapperImpl authorViewAMQPMapper;

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

    @PactVerifyProvider("an author created event")
    public MessageAndMetadata authorCreated() throws JsonProcessingException {
        LOGGER.info("Invoking authorCreated provider method");

        Author author = new Author("Pedro Miguel", "O melhor da cidade da Maia", null, "2");
        author.setAuthorNumber("3");
        author.setVersion(1L);
        authorEventPublisher.sendAuthorCreated(author);
        System.out.println("Author created event sent");

        AuthorViewAMQP authorViewAMQP = new AuthorViewAMQP();
        authorViewAMQP.setName(author.getName());
        authorViewAMQP.setBio(author.getBio());
        authorViewAMQP.setAuthorNumber(author.getAuthorNumber());
        authorViewAMQP.setPhotoURI(author.getPhotoURI());
        authorViewAMQP.setVersion(author.getVersion().toString());

        Message<String> message = new AuthorMessageBuilder().withAuthor(authorViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    @PactVerifyProvider("an author updated event")
    public MessageAndMetadata authorUpdated() throws JsonProcessingException {
        LOGGER.info("Invoking authorUpdated provider method");

        Author author = new Author("Pedro Miguel", "O melhor da cidade da Maia", null, "2");
        author.setAuthorNumber("3");
        author.setVersion(1L);
        authorEventPublisher.sendAuthorUpdated(author, author.getVersion());
        System.out.println("Author created event sent");

        AuthorViewAMQP authorViewAMQP = new AuthorViewAMQP();
        authorViewAMQP.setName(author.getName());
        authorViewAMQP.setBio(author.getBio());
        authorViewAMQP.setAuthorNumber(author.getAuthorNumber());
        authorViewAMQP.setPhotoURI(author.getPhotoURI());
        authorViewAMQP.setVersion(author.getVersion().toString());

        Message<String> message = new AuthorMessageBuilder().withAuthor(authorViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
        HashMap<String, Object> metadata = new HashMap<>();
        message.getHeaders().forEach((k, v) -> metadata.put(k, v));

        return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
    }
}
