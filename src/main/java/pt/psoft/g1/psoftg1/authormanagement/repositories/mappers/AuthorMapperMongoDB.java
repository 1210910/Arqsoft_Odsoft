package pt.psoft.g1.psoftg1.authormanagement.repositories.mappers;

import org.mapstruct.Mapper;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.mongodb.AuthorMongoDB;
import pt.psoft.g1.psoftg1.shared.model.Photo;


@Mapper(componentModel = "spring")
public interface AuthorMapperMongoDB {

    AuthorMongoDB toMongoDB(Author author);

    Author toDomain(AuthorMongoDB authorMongoDB);

    String map(Photo value);
}
