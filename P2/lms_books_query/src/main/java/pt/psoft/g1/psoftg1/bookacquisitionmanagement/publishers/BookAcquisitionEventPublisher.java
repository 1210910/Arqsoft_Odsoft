package pt.psoft.g1.psoftg1.bookacquisitionmanagement.publishers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.api.BookAcquisitionViewAMQP;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.api.BookAcquisitionViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.BookAcquisition;
import pt.psoft.g1.psoftg1.shared.model.BookAcquisitionEvents;

@Service
@RequiredArgsConstructor
public class BookAcquisitionEventPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    private final BookAcquisitionViewAMQPMapper bookAcquisitionViewAMQPMapper;

    private int count = 0;


    public void sendBookAcquisitionCreated(BookAcquisition bookAcquisition) {
        sendBookAcquisitionEvent(bookAcquisition, bookAcquisition.getVersion(), BookAcquisitionEvents.BOOK_ACQUISITION_CREATED);
    }


    public void sendBookAcquisitionUpdated(BookAcquisition bookAcquisition, Long currentVersion) {
        sendBookAcquisitionEvent(bookAcquisition, currentVersion, BookAcquisitionEvents.BOOK_ACQUISITION_UPDATED);
    }


    public void sendBookAcquisitionDeleted(BookAcquisition bookAcquisition, Long currentVersion) {
        sendBookAcquisitionEvent(bookAcquisition, currentVersion, BookAcquisitionEvents.BOOK_ACQUISITION_DELETED);
    }

    public void sendBookAcquisitionEvent(BookAcquisition bookAcquisition, Long currentVersion, String bookAcquisitionEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            BookAcquisitionViewAMQP bookAcquisitionViewAMQP = bookAcquisitionViewAMQPMapper.toBookAcquisitionViewAMQP(bookAcquisition);
            bookAcquisitionViewAMQP.setVersion(currentVersion.toString());

            String jsonString = objectMapper.writeValueAsString(bookAcquisitionViewAMQP);

            this.template.convertAndSend(direct.getName(), bookAcquisitionEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending book acquisition event: '" + ex.getMessage() + "'");
        }
    }
}

