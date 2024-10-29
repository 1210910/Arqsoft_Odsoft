package pt.psoft.g1.psoftg1.authormanagement.model.relational;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.model.Bio;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.relational.EntityWithPhotoEntity;
import pt.psoft.g1.psoftg1.shared.model.relational.NameEntity;

@Entity
@Table(name = "Author")
public class AuthorEntity extends EntityWithPhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AUTHOR_NUMBER")
    @Getter
    private Long authorNumber;

    @Version
    private long version;

    @Embedded
    private NameEntity name;

    @Embedded
    private BioEntity bio;

    public void setName(String name) {
        this.name = new NameEntity(name);
    }

    public void setBio(String bio) {
        this.bio = new BioEntity(bio);
    }

    public Long getVersion() {
        return version;
    }

    public Long getId() {
        return authorNumber;
    }

    @Builder
    public AuthorEntity(String name, String bio, String photoURI) {
        setName(name);
        setBio(bio);
        setPhotoInternal(photoURI);
    }

    protected AuthorEntity() {
        // got ORM only
    }
    public String getName() {
        return this.name.toString();
    }

    public String getBio() {
        return this.bio.toString();
    }
}

