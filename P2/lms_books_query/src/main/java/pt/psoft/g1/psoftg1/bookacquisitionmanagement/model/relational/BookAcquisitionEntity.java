package pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.relational;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pt.psoft.g1.psoftg1.authormanagement.model.relational.AuthorEntity;
import pt.psoft.g1.psoftg1.genremanagement.model.relational.GenreEntity;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BookAcquisition")
public class BookAcquisitionEntity extends EntityWithPhoto {
    @Getter
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long pk;

    @Version
    @Getter
    private Long version;

    @Embedded
    AcqIdEntity acqIdEntity;

    @Getter
    @Embedded
    @NotNull
    TitleEntity title;

    @Getter
    @ManyToOne
    @NotNull
    GenreEntity genre;

    @Getter
    @ManyToMany
    private List<AuthorEntity> authors = new ArrayList<>();

    @Embedded
    DescriptionEntity description;

    private void setTitle(String title) {this.title = new TitleEntity(title);}

    private void setDescription(String description) {this.description = new DescriptionEntity(description); }

    private void setAcqID(String acqID) {
        this.acqIdEntity = new AcqIdEntity(acqID);
    }

    public void setGenre(GenreEntity genre) {this.genre = genre; }

    public void setAuthors(List<AuthorEntity> authors) {this.authors = authors; }

    public String getDescription(){ return this.description.toString(); }

    public BookAcquisitionEntity(String acqID, String title, String description, GenreEntity genre, List<AuthorEntity> authors, String photoURI) {
        setTitle(title);
        setAcqID(acqID);
        if(description != null)
            setDescription(description);
        if(genre==null)
            throw new IllegalArgumentException("Genre cannot be null");
        setGenre(genre);
        if(authors == null)
            throw new IllegalArgumentException("Author list is null");
        if(authors.isEmpty())
            throw new IllegalArgumentException("Author list is empty");

        setAuthors(authors);
        setPhotoInternal(photoURI);
    }

    protected BookAcquisitionEntity() {
        // got ORM only
    }

    public String getAcqID(){
        return this.acqIdEntity.toString();
    }
}
