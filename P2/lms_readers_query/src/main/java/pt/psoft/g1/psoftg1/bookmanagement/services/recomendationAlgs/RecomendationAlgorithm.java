package pt.psoft.g1.psoftg1.bookmanagement.services.recomendationAlgs;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;

public interface RecomendationAlgorithm {
   List<Book> recommend(String readerNumber);
}


