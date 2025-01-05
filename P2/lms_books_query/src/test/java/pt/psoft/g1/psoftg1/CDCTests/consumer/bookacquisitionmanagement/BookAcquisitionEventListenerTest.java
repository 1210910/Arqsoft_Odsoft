package pt.psoft.g1.psoftg1.lms_book_command.consumer.bookacquisitionmanagement;

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
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.api.BookAcquisitionViewAMQP;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.api.BookAcquisitionViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.listeners.BookAcquisitionEventListener;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.publishers.BookAcquisitionEventPublisher;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.services.BookAcquisitionService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = BookAcquisitionEventListener.class)
@PactConsumerTest
@PactTestFor(providerName = "bookAcquisition_event-producer", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
public class BookAcquisitionEventListenerTest {

    @MockBean
    BookAcquisitionService bookAcquisitionService;

    @MockBean
    BookAcquisitionEventPublisher bookAcquisitionEventPublisher;

    @MockBean
    BookAcquisitionViewAMQPMapper bookAcquisitionViewAMQPMapper;

    @Qualifier("bookAcquisitionEventListener")
    @Autowired
    BookAcquisitionEventListener bookAcquisitionEventListener;

    // Contrato para evento "Book Acquisition Created"
    @Pact(consumer = "bookAcquisition_created-consumer")
    V4Pact createBookAcquisitionCreatedPact(MessagePactBuilder builder) {
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

        return builder.expectsToReceive("a book acquisition created event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    // Contrato para evento "Book Acquisition Updated"
    @Pact(consumer = "bookAcquisition_updated-consumer")
    V4Pact createBookAcquisitionUpdatedPact(MessagePactBuilder builder) {
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

        return builder.expectsToReceive("a book acquisition updated event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    // Teste do evento "Book Acquisition Created"
    @Test
    @PactTestFor(pactMethod = "createBookAcquisitionCreatedPact")
    void testBookCreated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            bookAcquisitionEventListener.receiveBookAcquisitionCreated(message);
        });

        verify(bookAcquisitionService, times(1)).create(any(BookAcquisitionViewAMQP.class));
    }

    // Teste do evento "Book Updated"
    @Test
    @PactTestFor(pactMethod = "createBookAcquisitionUpdatedPact")
    void testBookUpdated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            bookAcquisitionEventListener.receiveBookAcquisitionUpdated(message);
        });

        verify(bookAcquisitionService, times(1)).update(any(BookAcquisitionViewAMQP.class));
    }
}