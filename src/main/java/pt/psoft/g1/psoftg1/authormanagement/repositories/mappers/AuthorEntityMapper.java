package pt.psoft.g1.psoftg1.authormanagement.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.relational.AuthorEntity;
import pt.psoft.g1.psoftg1.shared.model.Photo;

@Mapper(componentModel = "spring")
public interface AuthorEntityMapper {

    @Mapping(target = "authorNumber", source = "authorNumber")
    Author toDomain(AuthorEntity authorEntity);

    AuthorEntity toEntity(Author author);

    String map(Photo value);
}
