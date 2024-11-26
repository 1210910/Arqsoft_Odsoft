package pt.psoft.g1.psoftg1.bookmanagement.publishers;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;

@Service
@RequiredArgsConstructor
public class BookEventPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    private final BookViewAMQPMapper bookViewAMQPMapper;

    private int count = 0;


    public void sendBookCreated(Book book) {
        sendBookEvent(book, book.getVersion(), BookEvents.BOOK_CREATED);
    }


    public void sendBookUpdated(Book book, Long currentVersion) {
        sendBookEvent(book, currentVersion, BookEvents.BOOK_UPDATED);
    }


    public void sendBookDeleted(Book book, Long currentVersion) {
        sendBookEvent(book, currentVersion, BookEvents.BOOK_DELETED);
    }

    public void sendBookEvent(Book book, Long currentVersion, String bookEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            BookViewAMQP bookViewAMQP = bookViewAMQPMapper.toBookViewAMQP(book);
            bookViewAMQP.setVersion(currentVersion.toString());

            String jsonString = objectMapper.writeValueAsString(bookViewAMQP);

            this.template.convertAndSend(direct.getName(), bookEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending book event: '" + ex.getMessage() + "'");
        }
    }
}

