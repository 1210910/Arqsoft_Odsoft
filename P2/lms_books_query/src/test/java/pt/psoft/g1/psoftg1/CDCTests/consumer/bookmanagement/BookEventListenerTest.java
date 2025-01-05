package pt.psoft.g1.psoftg1.lms_book_command.consumer.bookmanagement;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.listeners.BookEventListener;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = BookEventListener.class)
@PactConsumerTest
@PactTestFor(providerName = "book_event-producer", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
public class BookEventListenerTest {

    @MockBean
    BookService bookService;

    @MockBean
    BookEventPublisher bookEventPublisher;

    @MockBean
    BookViewAMQPMapper bookViewAMQPMapper;

    @Qualifier("bookEventListener")
    @Autowired
    BookEventListener bookEventListener;

    // Contrato para evento "Book Created"
    @Pact(consumer = "book_created-consumer")
    V4Pact createBookCreatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        //List<String> authorIds = List.of("3");
        body.stringType("title", "Lewis Hamilton - GOAT of F1");
        body.array("authorIds")
                .stringType("3")
                .closeArray();
        body.stringType("isbn", "1439549719859");
        body.stringType("description", "The biography of the absolute best of Formula 1");
        body.stringType("genre", "Informação");
        body.stringType("version", "1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("a book created event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    // Contrato para evento "Book Updated"
    @Pact(consumer = "book_updated-consumer")
    V4Pact createBookUpdatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringType("title", "Lewis Hamilton - GOAT of F1");
        body.array("authorIds")
                .stringType("3")
                .closeArray();
        body.stringType("isbn", "1439549719859");
        body.stringType("description", "The biography of the absolute best of Formula 1");
        body.stringType("genre", "Informação");
        body.stringType("version", "1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("a book updated event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    // Teste do evento "Book Created"
    @Test
    @PactTestFor(pactMethod = "createBookCreatedPact")
    void testBookCreated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            bookEventListener.receiveBookCreated(message);
        });

        verify(bookService, times(1)).create(any(BookViewAMQP.class));
    }

    // Teste do evento "Book Updated"
    @Test
    @PactTestFor(pactMethod = "createBookUpdatedPact")
    void testBookUpdated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            bookEventListener.receiveBookUpdated(message);
        });

        verify(bookService, times(1)).update(any(BookViewAMQP.class));
    }
}