package pt.psoft.g1.psoftg1.lms_book_command.producer.genremanagement;

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
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.api.*;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.publishers.GenreEventPublisher;
import pt.psoft.g1.psoftg1.lms_book_command.producer.authormanagement.AuthorMessageBuilder;

import java.util.HashMap;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {GenreEventPublisher.class, GenreViewAMQPMapperImpl.class},
        properties = {
                "stubrunner.amqp.mockConnection=true",
                "spring.profiles.active=test"
        }
)
@Provider("genre_event-producer")
@PactFolder("target/pacts")
public class GenreEventPublisherTest {

    @Autowired
    private GenreEventPublisher genreEventPublisher;

    @Autowired
    private GenreViewAMQPMapperImpl genreViewAMQPMapper;

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

    @PactVerifyProvider("a genre created event")
    public MessageAndMetadata genreCreated() throws JsonProcessingException {

        Genre genre = new Genre("Fição Científica");
        genreEventPublisher.sendGenreCreated(genre);
        System.out.println("Genre created event sent");

        GenreViewAMQP genreViewAMQP = new GenreViewAMQP();
        genreViewAMQP.setGenre(genre.getGenre());

        Message<String> message = new GenreMessageBuilder().withGenre(genreViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
        HashMap<String, Object> metadata = new HashMap<>();
        message.getHeaders().forEach((k, v) -> metadata.put(k, v));

        return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
    }
}
