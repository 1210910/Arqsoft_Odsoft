package pt.psoft.g1.psoftg1.bookacquisitionmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookacquisitionmanagement.model.BookAcquisition;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class BookAcquisitionViewMapper extends MapperInterface {
    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "acqID", source = "acqID")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "title", source = "title")
    //@Mapping(target = "authors", expression = "java(mapAuthors(book.getAuthors()))")
    @Mapping(target = "_links", expression = "java(mapLinks(book))")
    public abstract BookAcquisitionView toBookAcquisitionView(BookAcquisition book);

    public abstract List<BookAcquisitionView> toBookAcquisitionView(List<BookAcquisition> bookList);

    protected List<String> mapAuthors(List<Author> authors) {
        return authors.stream()
                .map(Author::getName)
                .collect(Collectors.toList());
    }

    @Named(value = "mapBookAcquisitionsLinks")
    public Map<String, Object> mapLinks(final BookAcquisition bookAcquisition) {
        String bookAcquisitionUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/acquisitions/")
                .path(bookAcquisition.getAcqID())
                .toUriString();

        Map<String, Object> links = new HashMap<>();
        links.put("self", bookAcquisitionUri);

        List<Map<String, String>> authorLinks = bookAcquisition.getAuthors().stream()
                .map(author -> {
                    String authorUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/authors/")
                            .path(author.getAuthorNumber().toString())
                            .toUriString();
                    Map<String, String> authorLink = new HashMap<>();
                    authorLink.put("href", authorUri);
                    return authorLink;
                })
                .collect(Collectors.toList());

        links.put("authors", authorLinks);
        links.put("photo", generatePhotoUrl(bookAcquisition));

        return links;
    }


    protected String generatePhotoUrl(BookAcquisition bookAcquisition) {
        String pk = bookAcquisition.getPk().toString();
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/acquisitions/photo").buildAndExpand(pk).toUri().toString();
    }
}
