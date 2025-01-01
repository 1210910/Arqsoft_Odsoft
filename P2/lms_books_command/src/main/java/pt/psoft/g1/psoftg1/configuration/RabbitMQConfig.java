package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.psoft.g1.psoftg1.authormanagement.listeners.AuthorEventListener;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.listeners.BookEventListener;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.genremanagement.listeners.GenreEventListener;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.shared.model.AuthorEvents;
import pt.psoft.g1.psoftg1.shared.model.BookAcquisitionEvents;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;
import pt.psoft.g1.psoftg1.shared.model.GenreEvents;

@Configuration
public class RabbitMQConfig {

    // Definindo uma exchange
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("LMS.direct");
    }

    // Definindo filas para eventos de livros, autores e gêneros

    @Bean
    public Queue bookLendingRequestQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue bookLendingResponseQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue bookCreatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue bookUpdatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue bookDeletedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue bookAcquisitionCreatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue bookAcquisitionUpdatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue bookAcquisitionDeletedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue authorCreatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue authorUpdatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue authorDeletedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue genreCreatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue genreUpdatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue genreDeletedQueue() {
        return new AnonymousQueue();
    }


    @Bean
    public Binding bookLendingRequestBinding(DirectExchange direct,
                                             @Qualifier("bookLendingRequestQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(direct)
                .with("book.lending.requests");
    }

    @Bean
    public Binding bookLendingResponseBinding(DirectExchange direct,
                                              @Qualifier("bookLendingResponseQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(direct)
                .with("book.lending.responses");
    }

    @Bean
    public Binding bookCreatedBinding(DirectExchange direct,
                                      @Qualifier("bookCreatedQueue") Queue bookCreatedQueue) {
        return BindingBuilder.bind(bookCreatedQueue)
                .to(direct)
                .with(BookEvents.BOOK_CREATED);
    }

    @Bean
    public Binding bookUpdatedBinding(DirectExchange direct,
                                      @Qualifier("bookUpdatedQueue") Queue bookUpdatedQueue) {
        return BindingBuilder.bind(bookUpdatedQueue)
                .to(direct)
                .with(BookEvents.BOOK_UPDATED);
    }

    @Bean
    public Binding bookDeletedBinding(DirectExchange direct,
                                      @Qualifier("bookDeletedQueue") Queue bookDeletedQueue) {
        return BindingBuilder.bind(bookDeletedQueue)
                .to(direct)
                .with(BookEvents.BOOK_DELETED);
    }

    @Bean
    public Binding bookAcquisitionCreatedBinding(DirectExchange direct,
                                                 @Qualifier("bookAcquisitionCreatedQueue") Queue bookAcquisitionCreatedQueue){
        return BindingBuilder.bind(bookAcquisitionCreatedQueue)
                .to(direct)
                .with(BookAcquisitionEvents.BOOK_ACQUISITION_CREATED);
    }

    @Bean
    public Binding bookAcquisitionUpdatedBinding(DirectExchange direct,
                                                 @Qualifier("bookAcquisitionUpdatedQueue") Queue bookAcquisitionUpdatedQueue){
        return BindingBuilder.bind(bookAcquisitionUpdatedQueue)
                .to(direct)
                .with(BookAcquisitionEvents.BOOK_ACQUISITION_UPDATED);
    }

    @Bean
    public Binding bookAcquisitionDeletedBinding(DirectExchange direct,
                                                 @Qualifier("bookAcquisitionDeletedQueue") Queue bookAcquisitionDeletedQueue){
        return BindingBuilder.bind(bookAcquisitionDeletedQueue)
                .to(direct)
                .with(BookAcquisitionEvents.BOOK_ACQUISITION_DELETED);
    }

    @Bean
    public Binding authorCreatedBinding(DirectExchange direct,
                                        @Qualifier("authorCreatedQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(direct)
                .with(AuthorEvents.AUTHOR_CREATED);
    }

    @Bean
    public Binding authorUpdatedBinding(DirectExchange direct,
                                        @Qualifier("authorUpdatedQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(direct)
                .with(AuthorEvents.AUTHOR_UPDATED);
    }

    @Bean
    public Binding authorDeletedBinding(DirectExchange direct,
                                        @Qualifier("authorDeletedQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(direct)
                .with(AuthorEvents.AUTHOR_DELETED);
    }

    @Bean
    public Binding genreCreatedBinding(DirectExchange direct,
                                       @Qualifier("genreCreatedQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(direct)
                .with(GenreEvents.GENRE_CREATED);
    }


    @Bean
    public Binding genreDeletedBinding(DirectExchange direct,
                                       @Qualifier("genreDeletedQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(direct)
                .with(GenreEvents.GENRE_DELETED);
    }


    @Bean
    public BookEventListener bookReceiver(BookService bookService, BookViewAMQPMapper bookViewAMQPMapper, BookEventPublisher bookEventPublisher) {
        return new BookEventListener (bookService, bookViewAMQPMapper, bookEventPublisher);
    }

    @Bean
    public AuthorEventListener authorReceiver (AuthorService authorService) {
        return new AuthorEventListener(authorService);
    }

    @Bean
    public GenreEventListener genreReceiver (GenreService genreService) {
        return new GenreEventListener(genreService);
    }


}