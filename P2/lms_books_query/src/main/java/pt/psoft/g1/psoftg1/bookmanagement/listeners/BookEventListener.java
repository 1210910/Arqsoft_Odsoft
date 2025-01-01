package pt.psoft.g1.psoftg1.bookmanagement.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookSagaViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.api.SagaCreationResponse;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class BookEventListener {
    private final BookService bookService;
    private final BookViewAMQPMapper bookViewAMQPMapper;
    private final BookEventPublisher bookEventPublisher;

    @RabbitListener(queues = "#{bookCreatedQueue.name}")
    public void receiveBookCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            BookViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, BookViewAMQP.class);

            System.out.println(" [x] Received Book Created by AMQP: " + msg + ".");
            try {
                bookService.create(bookViewAMQP);
                System.out.println(" [x] New book inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(" [x] Book already exists. No need to store it.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving book event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{bookUpdatedQueue.name}")
    public void receiveBookUpdated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            BookViewAMQP bookViewAMQP = objectMapper.readValue(jsonReceived, BookViewAMQP.class);

            System.out.println(" [x] Received Book Updated by AMQP: " + msg + ".");
            try {
                bookService.update(bookViewAMQP);
                System.out.println(" [x] Book updated from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Book does not exists or wrong version. Nothing stored.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving book event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{bookDeletedQueue.name}")
    public void receiveBookDeleted(String in) {
        System.out.println(" [x] Received Book Deleted '" + in + "'");
    }

    @RabbitListener(queues = "#{bookLendingRequestQueue.name}")
    public void receiveBookLendingRequest(Message msg) {
        SagaCreationResponse response = new SagaCreationResponse();


        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);

            BookSagaViewAMQP bookSagaViewAMQP = objectMapper.readValue(jsonReceived, BookSagaViewAMQP.class);

            System.out.println(" [x] Received book Lending Request by AMQP: " + msg + ".");

            BookViewAMQP bookViewAMQP = bookViewAMQPMapper.toBookViewAMQP(bookSagaViewAMQP);

            Book book = null;

            System.out.println(" [x] Received book Lending Request by AMQP: " + msg + ".");
            try {
                book=bookService.create(bookViewAMQP);
                System.out.println(" [x] New book inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(" [x] book already exists. No need to store it.");
                response.setStatus("ERROR");
                response.setLendingNumber(bookSagaViewAMQP.getLendingNumber());
                response.setError(e.getMessage());
                bookEventPublisher.sendBookLendingResponse(response);
            }

            response.setLendingNumber(bookSagaViewAMQP.getLendingNumber());
            response.setStatus("SUCCESS");

            bookEventPublisher.sendBookLendingResponse(response);
            bookEventPublisher.sendBookCreated(book);

        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving reader lending event from AMQP: '" + ex.getMessage() + "'");
        }
    }
}