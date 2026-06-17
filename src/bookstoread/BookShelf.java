package bookstoread;
import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookShelf {
    // Création de la variable d'instance pour stocker les livres
    private final List<Book> books = new ArrayList<>();

    // Modification pour retourner une vue immuable de la liste
    public List<Book> books() {
        return Collections.unmodifiableList(books);
    }


    // Utilisation d'une méthode avec un vararg
    public void add(Book... booksToAdd) {
        books.addAll(Arrays.asList(booksToAdd));
    }

    // Ajout de la méthode pour la compilation
    public List<Book> arrange() {
        return arrange(Comparator.naturalOrder());
    }

    public List<Book> arrange(Comparator<Book> criteria) {
        return books.stream().sorted(criteria).collect(Collectors.toList());
    }

    // Utilisation de la méthode générique pour éviter la duplication
    public Map<Year, List<Book>> groupByPublicationYear() {
        return this.groupBy(book -> Year.of(book.getPublishedOn().getYear()));
    }

    //  Regroupement générique basé sur une fonction fournie par le client
    public <K> Map<K, List<Book>> groupBy(Function<Book, K> fx) {
        return books.stream().collect(Collectors.groupingBy(fx));
    }
}