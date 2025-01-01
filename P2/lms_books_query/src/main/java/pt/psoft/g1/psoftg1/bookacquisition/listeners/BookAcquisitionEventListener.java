package pt.psoft.g1.psoftg1.bookacquisition.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookacquisition.api.BookAcquisitionViewAMQP;
import pt.psoft.g1.psoftg1.bookacquisition.services.BookAcquisitionService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class BookAcquisitionEventListener {
    private final BookAcquisitionService bookAcquisitionService;

    @RabbitListener(queues = "#{bookAcquisitionCreatedQueue.name}")
    public void receiveBookAcquisitionCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            BookAcquisitionViewAMQP bookAcquisitionViewAMQP = objectMapper.readValue(jsonReceived, BookAcquisitionViewAMQP.class);

            System.out.println(" [x] Received Book Acquisition Created by AMQP: " + msg + ".");
            try {
                bookAcquisitionService.create(bookAcquisitionViewAMQP);
                System.out.println(" [x] New Book Acquisition inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Book Acquisition already exists. No need to store it.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving book acquisition event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{bookAcquisitionUpdatedQueue.name}")
    public void receiveBookAcquisitionUpdated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            BookAcquisitionViewAMQP bookAcquisitionViewAMQP = objectMapper.readValue(jsonReceived, BookAcquisitionViewAMQP.class);

            System.out.println(" [x] Received Book Acquisition Updated by AMQP: " + msg + ".");
            try {
//                bookAcquisitionService.update(bookAcquisitionViewAMQP);
                System.out.println(" [x] Book Acquisition updated from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Book Acquisition does not exists or wrong version. Nothing stored.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving book acquisition event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{bookAcquisitionDeletedQueue.name}")
    public void receiveBookAcquisitionDeleted(String in) {
        System.out.println(" [x] Received Book Acquisition Deleted '" + in + "'");
    }
}