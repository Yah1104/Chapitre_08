package bookstoread;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BookShelfSpec {
    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;

    @BeforeEach
    void init() throws Exception {
        shelf = new BookShelf();
        effectiveJava = new Book("Effective Java", "Joshua Bloch", LocalDate.of(2008, Month.MAY, 8));
        codeComplete = new Book("Code Complete", "Steve McConnel", LocalDate.of(2004, Month.JUNE, 9));
        mythicalManMonth = new Book("The Mythical Man-Month", "Frederick Phillips Brooks", LocalDate.of(1975, Month.JANUARY, 1));
        cleanCode = new Book("Clean Code", "Robert C. Martin", LocalDate.of(2008, Month.AUGUST, 1));
    }

    @Test
    public void shelfEmptyWhenNoBookAdded() throws Exception {
        List<Book> books = shelf.books(); // Corrigé : utilise la variable globale initialisée
        assertTrue(books.isEmpty(), () -> "BookShelf should be empty.");
    }

    @Test
    void bookshelfContainsTwoBooksWhenTwoBooksAdded() {
        shelf.add(effectiveJava, codeComplete); // Corrigé : passe les instances de Book, pas des String
        List<Book> books = shelf.books();
        assertEquals(2, books.size(), () -> "BookShelf should have two books.");
    }

    @Test
    public void emptyBookShelfWhenAddIsCalledWithoutBooks() {
        shelf.add();
        List<Book> books = shelf.books();
        assertTrue(books.isEmpty(), () -> "BookShelf should be empty.");
    }

    @Test
    void booksReturnedFromBookShelfIsImmutableForClient() {
        shelf.add(effectiveJava, codeComplete); // Corrigé : passe les instances de Book
        List<Book> books = shelf.books();
        try {
            books.add(mythicalManMonth); // Corrigé : minuscule 'm'
            fail(() -> "Should not be able to add book to books");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException, () -> "Should throw UnsupportedOperationException.");
        }
    }

    @Test
    void bookshelfArrangedByBookTitle() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth); // Corrigé : passe les instances de Book
        List<Book> books = shelf.arrange();
        // Code Complete, Effective Java, The Mythical Man-Month
        assertEquals(Arrays.asList(codeComplete, effectiveJava, mythicalManMonth), books,
                () -> "Books in a bookshelf should be arranged lexicographically by book title");
    }

    @Test
    void booksInBookShelfAreInInsertionOrderAfterCallingArrange() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth); //  passe les instances de Book
        shelf.arrange();
        List<Book> books = shelf.books();
        assertEquals(Arrays.asList(effectiveJava, codeComplete, mythicalManMonth), books,
                () -> "Books in bookshelf are in insertion order");
    }

    @Test
    void bookshelfArrangedByUserProvidedCriteria() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        // Tri ordre alphabétique inverse (Z à A)
        List<Book> books = shelf.arrange(Comparator.<Book>naturalOrder().reversed());

        // L'ordre inverse attendu est : The Mythical Man-Month, Effective Java, Code Complete
        assertEquals(Arrays.asList(mythicalManMonth, effectiveJava, codeComplete), books,
                () -> "Books in a bookshelf are arranged in descending order of book title");
    }

    @Test
    void bookshelfArrangedByPublishedDate() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        // Tri par date de publication (du plus ancien au plus récent)
        List<Book> books = shelf.arrange(Comparator.comparing(Book::getPublishedOn));

        // Ordre attendu : Mythical Man-Month (1975), Code Complete (2004), Effective Java (2008)
        assertEquals(Arrays.asList(mythicalManMonth, codeComplete, effectiveJava), books,
                () -> "Books in a bookshelf should be arranged by publication date");
    }

    @Test
    @DisplayName("books inside bookshelf are grouped by publication year")
    void groupBooksInsideBookShelfByPublicationYear() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
        Map<Year, List<Book>> booksByPublicationYear = shelf.groupByPublicationYear();

        // Vérification avec AssertJ
        assertThat(booksByPublicationYear).containsKey(Year.of(2008)).containsValues(Arrays.asList(effectiveJava, cleanCode));
        assertThat(booksByPublicationYear).containsKey(Year.of(2004)).containsValues(Collections.singletonList(codeComplete));
        assertThat(booksByPublicationYear).containsKey(Year.of(1975)).containsValues(Collections.singletonList(mythicalManMonth));
    }


    @Test
    @DisplayName("Les livres à l'intérieur de la bibliothèque sont regroupés selon les critères fournis par l'utilisateur (regroupés par nom d'auteur)")
    void groupBooksByUserProvidedCriteria() {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);

        // Appel de la méthode générique en passant la référence de méthode getAuthor
        Map<String, List<Book>> booksByAuthor = shelf.groupBy(Book::getAuthor);

        // Assertions fluides avec AssertJ
        assertThat(booksByAuthor).containsKey("Joshua Bloch").containsValues(Collections.singletonList(effectiveJava));
        assertThat(booksByAuthor).containsKey("Steve McConnel").containsValues(Collections.singletonList(codeComplete));
        assertThat(booksByAuthor).containsKey("Frederick Phillips Brooks").containsValues(Collections.singletonList(mythicalManMonth));
        assertThat(booksByAuthor).containsKey("Robert C. Martin").containsValues(Collections.singletonList(cleanCode));
    }



}
