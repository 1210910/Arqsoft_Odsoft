package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.psoft.g1.psoftg1.authormanagement.listeners.AuthorEventListener;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.bookmanagement.listeners.BookEventListener;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.genremanagement.listeners.GenreEventListener;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.lendingmanagement.listeners.LendingEventListener;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.listeners.ReaderEventListener;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.*;

@Configuration
public class RabbitMQConfig {

    // Definindo uma exchange
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("LMS.books");
    }

    // Definindo filas para eventos de livros, autores e gÃªneros
    private static class ReceiverConfig {

        @Bean
        public Queue readerServiceInstanciatedQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Book_Created() {
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
        public Queue authorCreatedQueue(){
            return new AnonymousQueue();
        }

        @Bean
        public Queue authorUpdatedQueue(){
            return new AnonymousQueue();
        }

        @Bean
        public Queue authorDeletedQueue(){
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
        public Queue lendingCreatedQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue lendingUpdatedQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue lendingDeletedQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue readerCreatedQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue readerUpdatedQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue readerDeletedQueue() {
            return new AnonymousQueue();
        }


        // Definindo bindings para as filas

//        @Bean
//        public Binding readerServiceInstanciatedBinding(DirectExchange direct,
//                                                        @Qualifier("readerServiceInstanciatedQueue") Queue readerServiceInstanciatedQueue) {
//            return BindingBuilder.bind(readerServiceInstanciatedQueue)
//                    .to(direct)
//                    .with("ReaderServiceInstanciated");
//        }

        @Bean
        public Binding authorCreatedBinding(DirectExchange direct,
                                            @Qualifier("authorCreatedQueue") Queue authorCreatedQueue){
            return BindingBuilder.bind(authorCreatedQueue)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_CREATED);
        }

        @Bean
        public Binding authorUpdatedBinding(DirectExchange direct,
                                            @Qualifier("authorUpdatedQueue") Queue authorUpdatedQueue){
            return BindingBuilder.bind(authorUpdatedQueue)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_UPDATED);
        }

        @Bean
        public Binding authorDeleteBinding(DirectExchange direct,
                                           @Qualifier("authorDeletedQueue") Queue authorDeletedQueue){
            return BindingBuilder.bind(authorDeletedQueue)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_DELETED);
        }

        @Bean
        public Binding bookCreatedBinding(DirectExchange direct,
                                          @Qualifier("autoDeleteQueue_Book_Created") Queue autoDeleteQueue_Book_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Created)
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
        public Binding genreUpdatedBinding(DirectExchange direct,
                                           @Qualifier("genreUpdatedQueue") Queue queue) {
            return BindingBuilder.bind(queue)
                    .to(direct)
                    .with(GenreEvents.GENRE_UPDATED);
        }

        @Bean
        public Binding lendingCreatedBinding(DirectExchange direct,
                                             @Qualifier("lendingCreatedQueue") Queue queue) {
            return BindingBuilder.bind(queue)
                    .to(direct)
                    .with(LendingEvents.LENDING_CREATED);
        }

        @Bean
        public Binding lendingUpdatedBinding(DirectExchange direct,
                                             @Qualifier("lendingUpdatedQueue") Queue queue) {
            return BindingBuilder.bind(queue)
                    .to(direct)
                    .with(LendingEvents.LENDING_UPDATED);
        }

        @Bean
        public Binding lendingDeletedBinding(DirectExchange direct,
                                             @Qualifier("lendingDeletedQueue") Queue queue) {
            return BindingBuilder.bind(queue)
                    .to(direct)
                    .with(LendingEvents.LENDING_DELETED);
        }

        @Bean
        public Binding readerCreatedBinding(DirectExchange direct,
                                            @Qualifier("readerCreatedQueue") Queue queue) {
            return BindingBuilder.bind(queue)
                    .to(direct)
                    .with(ReaderEvents.READER_CREATED);
        }

        @Bean
        public Binding readerUpdatedBinding(DirectExchange direct,
                                            @Qualifier("readerUpdatedQueue") Queue queue) {
            return BindingBuilder.bind(queue)
                    .to(direct)
                    .with(ReaderEvents.READER_UPDATED);
        }

        @Bean
        public Binding readerDeletedBinding(DirectExchange direct,
                                            @Qualifier("readerDeletedQueue") Queue queue) {
            return BindingBuilder.bind(queue)
                    .to(direct)
                    .with(ReaderEvents.READER_DELETED);
        }

        @Bean
        public AuthorEventListener authorReceiver(AuthorService authorService){
            return new AuthorEventListener(authorService);
        }

        @Bean
        public BookEventListener bookReceiver(BookService bookService) {
            return new BookEventListener(bookService);
        }


        @Bean
        public GenreEventListener genreReceiver(GenreService genreService) {
            return new GenreEventListener(genreService);
        }


        @Bean
        public LendingEventListener lendingReceiver(LendingService lendingService) {
            return new LendingEventListener(lendingService);
        }

        @Bean
        public ReaderEventListener readerReceiver(ReaderService readerService) {
            return new ReaderEventListener(readerService);
        }
    }

}