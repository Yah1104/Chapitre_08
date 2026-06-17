package bookstoread;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookShelf {
    // Création de la variable d'instance pour stocker les livres
    private final List<String> books = new ArrayList<>();

    // Modification de la méthode pour retourner la variable d'instance
    public List<String> books() {
        return books;
    }


    public void add(String bookToAdd) {
        books.add(bookToAdd);
    }
}