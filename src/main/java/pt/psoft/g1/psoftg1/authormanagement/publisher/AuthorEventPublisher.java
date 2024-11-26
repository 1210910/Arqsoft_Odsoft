package pt.psoft.g1.psoftg1.authormanagement.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQPMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.model.AuthorEvents;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;

@Component
@RequiredArgsConstructor
public class AuthorEventPublisher {
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    private final AuthorViewAMQPMapper authorViewAMQPMapper;

    private int count = 0;


    public void sendAuthorCreated(Author author) {
        sendAuthorEvent(author, author.getVersion(), AuthorEvents.AUTHOR_CREATED);
    }


    public void sendAuthorUpdated(Author author, Long currentVersion) {
        sendAuthorEvent(author, currentVersion, AuthorEvents.AUTHOR_UPDATED);
    }


    public void sendAuthorDeleted(Author author, Long currentVersion) {
        sendAuthorEvent(author, currentVersion, AuthorEvents.AUTHOR_DELETED);
    }

    public void sendAuthorEvent(Author author, Long currentVersion, String authorEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            AuthorViewAMQP authorViewAMQP = authorViewAMQPMapper.toAuthorViewAMQP(author);
            authorViewAMQP.setVersion(currentVersion.toString());

            String jsonString = objectMapper.writeValueAsString(authorViewAMQP);

            this.template.convertAndSend(direct.getName(), authorEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending book event: '" + ex.getMessage() + "'");
        }
    }
}

