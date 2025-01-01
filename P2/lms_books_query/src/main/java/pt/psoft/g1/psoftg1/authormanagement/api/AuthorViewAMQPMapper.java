package pt.psoft.g1.psoftg1.authormanagement.api;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;


@Mapper(componentModel = "spring")
public interface AuthorViewAMQPMapper {


    AuthorViewAMQP toAuthorViewAMQP(Author author);


}
