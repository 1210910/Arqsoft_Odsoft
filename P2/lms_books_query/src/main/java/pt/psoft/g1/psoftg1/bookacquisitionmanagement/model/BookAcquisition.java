package pt.psoft.g1.psoftg1.bookacquisitionmanagement.model;

import lombok.Getter;


import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BookAcquisition extends EntityWithPhoto {

    @Getter
    @Setter
    private Long pk;

    @Setter
    @Getter
    private Long version;

    @Setter
    private AcqID acqID;

    @Getter
    private Title title;

    private Genre genre;

    @Getter
    private List<Author> authors = new ArrayList<>();

    Description description;

    private void setTitle(String title) {this.title = new Title(title);}

    private void setAcqID(String acqID) {
        this.acqID = new AcqID(acqID);
    }


    private void setDescription(String description) {this.description = new Description(description); }

    private void setGenre(Genre genre) {this.genre = genre; }

    private void setAuthors(List<Author> authors) {this.authors = authors; }

    public String getDescription(){ return this.description.toString(); }

    public BookAcquisition(String acqID, String title, String description, Genre genre, List<Author> authors, String photoURI) {
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

    protected BookAcquisition() {
        // got ORM only
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        setPhotoInternal(null);
    }

    public void applyPatch(final Long desiredVersion,
                           final String title,
                           final String description,
                           final String photoURI,
                           final Genre genre,
                           final List<Author> authors ) {

        if (!Objects.equals(this.version, desiredVersion))
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        if (title != null) {
            setTitle(title);
        }

        if (description != null) {
            setDescription(description);
        }

        if (genre != null) {
            setGenre(genre);
        }

        if (authors != null) {
            setAuthors(authors);
        }

        if (photoURI != null)
            setPhotoInternal(photoURI);

    }

    // Get genre
    public Genre getGenre(){
        return this.genre;
    }

    public String getAcqID(){
        return this.acqID.toString();
    }
}
