package pt.psoft.g1.psoftg1.bookacquisition.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.bookacquisition.model.BookAcquisition;
import pt.psoft.g1.psoftg1.bookacquisition.services.BookAcquisitionService;
import pt.psoft.g1.psoftg1.bookacquisition.services.CreateBookAcquisitionRequest;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

@Tag(name = "BookAcquisitions", description = "Endpoints for managing Book Acquisitions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/acquisitions")
public class BookAcquisitionController {
    private final BookAcquisitionService bookAcquisitionService;
    private final ConcurrencyService concurrencyService;
    private final FileStorageService fileStorageService;
    private final BookAcquisitionViewMapper bookAcquisitionViewMapper;

    //@Operation(summary = "Register a new Book Acquisition")
    //@PutMapping(value = "/{isbn}")
    //@ResponseStatus(HttpStatus.CREATED)
    //public ResponseEntity<BookAcquisitionView> create(CreateBookAcquisitionRequest resource, @PathVariable("isbn") String isbn) {
    //
    //    System.out.println("Entered on the Controller to create a new Book Acquisition");
    //
    //    //Guarantee that the client doesn't provide a link on the body, null = no photo or error
    //    resource.setPhotoURI(null);
    //    MultipartFile file = resource.getPhoto();
    //
    //    String fileName = fileStorageService.getRequestPhoto(file);
    //
    //    if (fileName != null) {
    //        resource.setPhotoURI(fileName);
    //    }
    //
    //    BookAcquisition bookAcquisition;
    //    try {
    //        bookAcquisition = bookAcquisitionService.create(resource, isbn);
    //        System.out.println("Passed the service bit of creating a new Book Acquisition");
    //        System.out.println("Title of the new Acquisition: " + bookAcquisition.getTitle());
    //        System.out.println("Pk of the new Acquisition: " + bookAcquisition.getPk());
    //    }catch (Exception e){
    //        System.out.println(e.getMessage());
    //        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //    }
    //    //final var savedBook = bookService.save(book);
    //
    //    // Construir o URI com base no `pk`
    //    return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
    //                    .path("/{isbn}")
    //                    .buildAndExpand(bookAcquisition.getIsbn())
    //                    .toUri())
    //            .body(bookAcquisitionViewMapper.toBookAcquisitionView(bookAcquisition));
    //}

//    @Operation(summary = "Deletes a book photo")
//    @DeleteMapping("/{isbn}/photo")
//    public ResponseEntity<Void> deleteBookPhoto(@PathVariable("isbn") final String isbn) {
//
//        var book = bookService.findByIsbn(isbn);
//        if(book.getPhoto() == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        fileStorageService.deleteFile(book.getPhoto().getPhotoFile());
//        bookService.removeBookPhoto(book.getIsbn(), book.getVersion());
//
//        return ResponseEntity.ok().build();
//    }

//    @Operation(summary= "Gets a book photo")
//    @GetMapping("/{isbn}/photo")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<byte[]> getSpecificBookPhoto(@PathVariable("isbn") final String isbn){
//
//        Book book = bookService.findByIsbn(isbn);
//
//        //In case the user has no photo, just return a 200 OK without body
//        if(book.getPhoto() == null) {
//            return ResponseEntity.ok().build();
//        }
//
//        String photoFile = book.getPhoto().getPhotoFile();
//        byte[] image = fileStorageService.getFile(photoFile);
//        String fileFormat = fileStorageService.getExtension(book.getPhoto().getPhotoFile()).orElseThrow(() -> new ValidationException("Unable to get file extension"));
//
//        if(image == null) {
//            return ResponseEntity.ok().build();
//        }
//
//        return ResponseEntity.ok().contentType(fileFormat.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG).body(image);
//
//    }
//
//
//    @Operation(summary = "Updates a specific Book")
//    @PatchMapping(value = "/{isbn}")
//    public ResponseEntity<BookView> updateBook(@PathVariable final String isbn,
//                                               final WebRequest request,
//                                               @Valid final UpdateBookRequest resource) {
//
//        final String ifMatchValue = request.getHeader(ConcurrencyService.IF_MATCH);
//        if (ifMatchValue == null || ifMatchValue.isEmpty() || ifMatchValue.equals("null")) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "You must issue a conditional PATCH using 'if-match'");
//        }
//
//        MultipartFile file = resource.getPhoto();
//
//        String fileName = fileStorageService.getRequestPhoto(file);
//
//        if (fileName != null) {
//            resource.setPhotoURI(fileName);
//        }
//
//        Book book;
//        resource.setIsbn(isbn);
//        try {
//            book = bookService.update(resource, String.valueOf(concurrencyService.getVersionFromIfMatchHeader(ifMatchValue)));
//        }catch (Exception e){
//            throw new ConflictException("Could not update book: "+ e.getMessage());
//        }
//        return ResponseEntity.ok()
//                .eTag(Long.toString(book.getVersion()))
//                .body(bookViewMapper.toBookView(book));
//    }
//
//    @Operation(summary = "Gets Books by title or genre")
//    @GetMapping
//    public ListResponse<BookView> findBooks(@RequestParam(value = "title", required = false) final String title,
//                                            @RequestParam(value = "genre", required = false) final String genre,
//                                            @RequestParam(value = "authorName", required = false) final String authorName) {
//
//        //Este método, como está, faz uma junção 'OR'.
//        //Para uma junção 'AND', ver o "/search"
//
//        List<Book> booksByTitle = null;
//        if (title != null)
//            booksByTitle = bookService.findByTitle(title);
//
//        List<Book> booksByGenre = null;
//        if (genre != null)
//            booksByGenre = bookService.findByGenre(genre);
//
//        List<Book> booksByAuthorName = null;
//        if (authorName != null)
//            booksByAuthorName = bookService.findByAuthorName(authorName);
//
//        System.out.println(booksByAuthorName);
//
//        Set<Book> bookSet = new HashSet<>();
//        if (booksByTitle!= null)
//            bookSet.addAll(booksByTitle);
//        if(booksByGenre != null)
//            bookSet.addAll(booksByGenre);
//        if(booksByAuthorName != null)
//            bookSet.addAll(booksByAuthorName);
//
//        List<Book> books = bookSet.stream()
//                .sorted(Comparator.comparing(b -> b.getTitle().toString()))
//                .collect(Collectors.toList());
//
//        if(books.isEmpty())
//            throw new NotFoundException("No books found with the provided criteria");
//
//        return new ListResponse<>(bookViewMapper.toBookView(books));
//    }
//
//    @Operation(summary = "Gets the top 5 books lent")
//    @GetMapping("top5")
//    public ListResponse<BookCountView> getTop5BooksLent() {
//        return new ListResponse<>(bookViewMapper.toBookCountView(bookService.findTop5BooksLent()));
//    }
//
//    @Operation(summary = "Gets some books suggestions based on the reader's interests")
//    @GetMapping("suggestions")
//    public ListResponse<BookView> getBooksSuggestions(Authentication authentication) {
//
//        // ReaderDetails readerDetails = readerService.findByUsername(loggedUser.getUsername())
//        //        .orElseThrow(() -> new NotFoundException(ReaderDetails.class, loggedUser.getUsername()));
//
//       // return new ListResponse<>(bookViewMapper.toBookView(bookService.getBooksSuggestionsForReader(readerDetails.getReaderNumber())));
//        return null;
//    }
//
//    @Operation(summary = "Get average lendings duration")
//    @GetMapping(value = "/{isbn}/avgDuration")
//    public @ResponseBody ResponseEntity<BookAverageLendingDurationView>getAvgLendingDurationByIsbn(
//            @PathVariable("isbn") final String isbn) {
//        final var book = bookService.findByIsbn(isbn);
//        Double avgDuration =0.0; //lendingService.getAvgLendingDurationByIsbn(isbn);
//
//        return ResponseEntity.ok().body(bookViewMapper.toBookAverageLendingDurationView(book, avgDuration));
//    }
//
//    @PostMapping("/search")
//    public ListResponse<BookView> searchBooks(
//            @RequestBody final SearchRequest<SearchBooksQuery> request) {
//        final var bookList = bookService.searchBooks(request.getPage(), request.getQuery());
//        return new ListResponse<>(bookViewMapper.toBookView(bookList));
//    }
}

