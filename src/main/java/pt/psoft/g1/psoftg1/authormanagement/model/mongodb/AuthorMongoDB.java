package pt.psoft.g1.psoftg1.authormanagement.model.mongodb;

import lombok.Builder;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.mongodb.EntityWithPhotoMongoDB;
import pt.psoft.g1.psoftg1.shared.model.mongodb.NameMongoDB;

@Document(collection = "authors")  // Optional: specify the collection name
public class AuthorMongoDB extends EntityWithPhotoMongoDB {

    @Id
    private String id;  // MongoDB uses String/ObjectId for IDs

    @Version
    private Long version;  // MongoDB versioning (optional, manual control)

    @Field("name")
    private NameMongoDB name;

    @Field("bio")
    private BioMongoDB bio;

    @Builder
    // Constructor, getters, setters
    public AuthorMongoDB(String name, String bio, String photoURI) {
        setName(name);
        setBio(bio);
        setPhotoInternal(photoURI);
    }

    protected AuthorMongoDB() {
        // for ORM or deserialization only
    }

    public void setName(String name) {
        this.name = new NameMongoDB(name);
    }

    public void setBio(String bio) {
        this.bio = new BioMongoDB(bio);
    }

    public void applyPatch(final long desiredVersion, final UpdateAuthorRequest request) {
        if (!this.version.equals(desiredVersion)) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        if (request.getName() != null) {
            setName(request.getName());
        }
        if (request.getBio() != null) {
            setBio(request.getBio());
        }
        if (request.getPhotoURI() != null) {
            setPhotoInternal(request.getPhotoURI());
        }
    }

    public void removePhoto(long desiredVersion) {
        if (desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }
        setPhotoInternal(null);
    }

    public String getName() {
        return this.name.toString();
    }

    public String getBio() {
        return this.bio.toString();
    }

    public String getId() {
        return id;
    }

    public String getPhotoURI() {
        return this.getPhoto().getPhotoFile().toString();
    }
}
