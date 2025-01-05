package pt.psoft.g1.psoftg1.lms_book_command.consumer.authormanagement;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
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
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.listeners.AuthorEventListener;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = AuthorEventListener.class)
@PactTestFor(providerName = "author_event-producer", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
public class AuthorEventListenerTest {

    @MockBean
    AuthorService authorService;

    @Qualifier("authorEventListener")
    @Autowired
    AuthorEventListener authorEventListener;

    // Contract for "Author Created" event
    @Pact(consumer = "author_created-consumer")
    V4Pact createAuthorCreatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringType("authorNumber", "1");
        body.stringType("name", "Pedro Miguel");
        body.stringType("bio", "O melhor da cidade da Maia");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("an author created event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    // Contract for "Author Updated" event
    @Pact(consumer = "author_updated-consumer")
    V4Pact createAuthorUpdatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringType("authorNumber", "1");
        body.stringType("name", "Pedro Miguel");
        body.stringType("bio", "O melhor da cidade da Maia");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("an author updated event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    // Test for "Author Created" event
    @Test
    @PactTestFor(pactMethod = "createAuthorCreatedPact")
    void testAuthorCreated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            authorEventListener.receiveBookCreated(message);
        });

        verify(authorService, times(1)).create(any(AuthorViewAMQP.class));
    }

    // Test for "Author Updated" event
    @Test
    @PactTestFor(pactMethod = "createAuthorUpdatedPact")
    void testAuthorUpdated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        String jsonReceived = messages.get(0).contentsAsString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

        assertDoesNotThrow(() -> {
            authorEventListener.receiveBookUpdated(message);
        });

        verify(authorService, times(1)).update(any(AuthorViewAMQP.class));
    }
}