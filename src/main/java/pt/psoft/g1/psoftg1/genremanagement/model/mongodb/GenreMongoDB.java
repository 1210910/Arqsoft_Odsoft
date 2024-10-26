package pt.psoft.g1.psoftg1.genremanagement.model.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "genres")
public class GenreMongoDB {

    @Id
    @Setter
    @Getter
    private String pk;

    @Field("genre")
    @Setter
    @Getter
    String genre;

    public GenreMongoDB(String genre){
        setGenre(genre);
    }

    protected GenreMongoDB(){
        // for ORM or deserialization only
    }

    private void setGenre(String genre) {
        if(genre == null)
            throw new IllegalArgumentException("Genre cannot be null");
        if(genre.isBlank())
            throw new IllegalArgumentException("Genre cannot be blank");
        int GENRE_MAX_LENGTH = 100;
        if(genre.length() > GENRE_MAX_LENGTH)
            throw new IllegalArgumentException("Genre has a maximum of 4096 characters");
        this.genre = genre;
    }

    public String toString() {
        return genre;
    }

}
