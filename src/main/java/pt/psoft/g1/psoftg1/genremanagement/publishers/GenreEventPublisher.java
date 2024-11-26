package pt.psoft.g1.psoftg1.genremanagement.publishers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQP;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQPMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.model.AuthorEvents;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;
import pt.psoft.g1.psoftg1.shared.model.GenreEvents;

@Component
@RequiredArgsConstructor
public class GenreEventPublisher {
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;
    private final GenreViewAMQPMapper genreViewAMQPMapper;

    private int count = 0;


    public void sendGenreCreated(Genre genre) {
        sendGenreEvent(genre, genre.getVersion(), GenreEvents.GENRE_UPDATED);
    }


    public void sendGenreUpdated(Genre genre, Long currentVersion) {
        sendGenreEvent(genre, currentVersion, GenreEvents.GENRE_UPDATED);
    }


    public void sendGenreDeleted(Genre genre, Long currentVersion) {
        sendGenreEvent(genre, currentVersion, GenreEvents.GENRE_DELETED);
    }

    public void sendGenreEvent(Genre genre, Long currentVersion, String genreEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            GenreViewAMQP genreViewAMQP = genreViewAMQPMapper.toGenreViewAMQP(genre);
            genreViewAMQP.setVersion(currentVersion.toString());

            String jsonString = objectMapper.writeValueAsString(genreViewAMQP);

            this.template.convertAndSend(direct.getName(), genreEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending book event: '" + ex.getMessage() + "'");
        }
    }
}
