package pt.psoft.g1.psoftg1.bookmanagement.repositories.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.model.relational.BookEntity;
import pt.psoft.g1.psoftg1.bookmanagement.model.relational.TitleEntity;
import pt.psoft.g1.psoftg1.shared.model.Photo;

@Mapper(componentModel = "spring")
public interface BookEntityMapper {

    @Mapping(target = "version", source = "entity.version")
    Book toModel(BookEntity entity);


    BookEntity toEntity(Book model);

    default String map(Photo value) {
        if (value == null) {
            return null;
        }
        return value.getPhotoFile(); // Exemplo para Photo
    }

    default String map(TitleEntity value) {
        if (value == null) {
            return null;
        }
        return value.getTitle(); // Exemplo para TitleEntity
    }

    default String map(Title value) {
        if (value == null) {
            return null;
        }
        return value.getTitle();  // Exemplo para Title
    }
}
