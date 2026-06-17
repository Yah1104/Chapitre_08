package bookstoread;
import java.util.*;
import java.util.stream.Collectors;

public class BookShelf {
    // Création de la variable d'instance pour stocker les livres
    private final List<String> books = new ArrayList<>();

    // Modification pour retourner une vue immuable de la liste
    public List<String> books() {
        return Collections.unmodifiableList(books);
    }


    // Refactoring : Utilisation d'une méthode avec un vararg
    public void add(String... booksToAdd) {
        books.addAll(Arrays.asList(booksToAdd));
    }

    // Ajout de la méthode pour la compilation
    public List<String> arrange() {
        return books.stream().sorted().collect(Collectors.toList());
    }
}