package pt.psoft.g1.psoftg1.bookmanagement.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.model.mongodb.BookMongoDB;
import pt.psoft.g1.psoftg1.bookmanagement.model.mongodb.TitleMongoDB;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.model.mongodb.GenreMongoDB;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.model.mongodb.PhotoMongoDB;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapperMongoDB {

    BookMongoDB toMongoDB(Book book);

    @Mapping(target = "version", source = "bookMongoDB.version")
    Book toDomain(BookMongoDB bookMongoDB);

    List<BookMongoDB> toMongoDBList(List<Book> books);

    List<Book> toDomainList(List<BookMongoDB> bookMongoDBs);

    default String map(Genre value){

        if (value == null){
            return null;
        }
        return value.getGenre(); // Exemplo para Genre
    }

    default String map(GenreMongoDB value){
        System.out.println("Entered GenreMongoDB");
        if (value == null){
            System.out.println("GenreMongoDB is null");
            return null;
        }
        System.out.println("GenreMongoDB is not null");
        return value.getGenre(); // Exemplo para Genre
    }

    default String map(Photo photo) {
        if (photo == null) {
            return null;
        }
        return photo.getPhotoFile();
    }

    default String map(PhotoMongoDB photoEntity) {
        if (photoEntity == null) {
            return null;
        }
        return photoEntity.getPhotoFile();
    }

    default String map(TitleMongoDB value) {
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
