package pt.psoft.g1.psoftg1.bookacquisition.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookacquisition.model.BookAcquisition;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class BookAcquisitionViewAMQPMapper extends MapperInterface {

    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authorIds", expression = "java(mapAuthors(book.getAuthors()))")
    @Mapping(target = "version", expression = "java(book.getVersion().toString())")
    public abstract BookAcquisitionViewAMQP toBookAcquisitionViewAMQP(BookAcquisition book);

    public abstract List<BookAcquisitionViewAMQP> toBookAcquisitionViewAMQP(List<BookAcquisition> bookList);

    protected List<String> mapAuthors(List<Author> authors) {
        return authors.stream().map(Author::getAuthorNumber).collect(Collectors.toList());
    }
}
