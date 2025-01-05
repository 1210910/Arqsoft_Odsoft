package pt.psoft.g1.psoftg1.CDCTests.producer.bookacquisitionmanagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.api.BookAcquisitionViewAMQP;

public class BookAcquisitionMessageBuilder {
    private ObjectMapper mapper = new ObjectMapper();
    private BookAcquisitionViewAMQP bookAcquisitionViewAMQP;

    public BookAcquisitionMessageBuilder withBookAcquisition(BookAcquisitionViewAMQP bookAcquisitionViewAMQP) {
        this.bookAcquisitionViewAMQP = bookAcquisitionViewAMQP;
        return this;
    }

    public Message<String> build() throws JsonProcessingException {
        return MessageBuilder.withPayload(this.mapper.writeValueAsString(this.bookAcquisitionViewAMQP))
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }
}
