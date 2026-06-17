package bookstoread;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Spécifications de l'étagère de livres (BookShelf)")
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


    // PREMIER GROUPE : CAS OÙ L'ÉTAGÈRE EST VIDE

    @Nested
    @DisplayName("Est vide")
    class IsEmpty {

        @Test
        @DisplayName("Quand aucun livre n'y est ajouté")
        public void emptyBookShelfWhenNoBookAdded() throws Exception {
            List<Book> books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf devrait être vide.");
        }

        @Test
        @DisplayName("Quand add est appelé sans livres")
        void emptyBookShelfWhenAddIsCalledWithoutBooks() {
            shelf.add();
            List<Book> books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf devrait être vide.");
        }
    }

    // DEUXIÈME GROUPE : APRÈS AJOUT DE LIVRES

    @Nested
    @DisplayName("Après avoir ajouté des livres")
    class BooksAreAdded {

        @Test
        @DisplayName("Contient deux livres")
        void bookshelfContainsTwoBooksWhenTwoBooksAdded() {
            shelf.add(effectiveJava, codeComplete);
            List<Book> books = shelf.books();
            assertEquals(2, books.size(), () -> "BookShelf devrait avoir deux livres.");
        }

        @Test
        @DisplayName("Renvoie au client une collection de livres immuable")
        void bookshelfIsImmutableForClient() {
            shelf.add(effectiveJava, codeComplete);
            List<Book> books = shelf.books();
            try {
                books.add(mythicalManMonth);
                fail(() -> "Le client ne devrait pas pouvoir modifier directement la liste.");
            } catch (Exception e) {
                assertTrue(e instanceof UnsupportedOperationException, () -> "Devrait lever une UnsupportedOperationException.");
            }
        }
    }

    // TROISIÈME GROUPE : OPÉRATIONS DE TRI

    @Nested
    @DisplayName("Lors du tri des livres")
    class SortingBooks {

        @Test
        @DisplayName("Sont organisés lexicographiquement par titre")
        void bookshelfArrangedByBookTitle() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange();
            assertEquals(Arrays.asList(codeComplete, effectiveJava, mythicalManMonth), books,
                    () -> "Les livres devraient être triés par titre.");
        }

        @Test
        @DisplayName("Préservent leur ordre d'insertion initial après un tri")
        void booksInBookShelfAreInInsertionOrderAfterCallingArrange() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            shelf.arrange();
            List<Book> books = shelf.books();
            assertEquals(Arrays.asList(effectiveJava, codeComplete, mythicalManMonth), books,
                    () -> "L'ordre d'insertion doit rester intact.");
        }

        @Test
        @DisplayName("Sont organisés selon les critères personnalisés de l'utilisateur")
        void bookshelfArrangedByUserProvidedCriteria() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange(Comparator.<Book>naturalOrder().reversed());
            assertEquals(Arrays.asList(mythicalManMonth, effectiveJava, codeComplete), books,
                    () -> "Les livres devraient être triés dans l'ordre inverse.");
        }

        @Test
        @DisplayName("Sont organisés par date de publication")
        void bookshelfArrangedByPublishedDate() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            List<Book> books = shelf.arrange(Comparator.comparing(Book::getPublishedOn));
            assertEquals(Arrays.asList(mythicalManMonth, codeComplete, effectiveJava), books,
                    () -> "Les livres devraient être triés par date.");
        }
    }

    // QUATRIÈME GROUPE : OPÉRATIONS DE REGROUPEMENT

    @Nested
    @DisplayName("Lors du regroupement des livres")
    class GroupingBooks {

        @Test
        @DisplayName("Sont regroupés correctement par année de publication")
        void groupBooksInsideBookShelfByPublicationYear() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
            Map<Year, List<Book>> booksByPublicationYear = shelf.groupByPublicationYear();

            assertThat(booksByPublicationYear).containsKey(Year.of(2008)).containsValues(Arrays.asList(effectiveJava, cleanCode));
            assertThat(booksByPublicationYear).containsKey(Year.of(2004)).containsValues(Collections.singletonList(codeComplete));
            assertThat(booksByPublicationYear).containsKey(Year.of(1975)).containsValues(Collections.singletonList(mythicalManMonth));
        }

        @Test
        @DisplayName("Sont regroupés selon un critère générique (ex: par auteur)")
        void groupBooksByUserProvidedCriteria() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
            Map<String, List<Book>> booksByAuthor = shelf.groupBy(Book::getAuthor);

            assertThat(booksByAuthor).containsKey("Joshua Bloch").containsValues(Collections.singletonList(effectiveJava));
            assertThat(booksByAuthor).containsKey("Steve McConnel").containsValues(Collections.singletonList(codeComplete));
            assertThat(booksByAuthor).containsKey("Frederick Phillips Brooks").containsValues(Collections.singletonList(mythicalManMonth));
            assertThat(booksByAuthor).containsKey("Robert C. Martin").containsValues(Collections.singletonList(cleanCode));
        }
    }
}
