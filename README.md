##  Gestionnaire d'Étagère de Livres (TDD & Conception Orientée Objet)

Ce projet pratique, réalisé dans le cadre du **Chapitre 08**, applique de manière rigoureuse la méthodologie **TDD (Test-Driven Development)** pour concevoir un système de gestion et d'organisation d'une étagère de livres (`BookShelf`).

L'objectif principal est de mettre en œuvre les concepts avancés du langage **Java 21**, notamment l'API Stream, la généricité, l'encapsulation défensive et l'écriture de tests hiérarchiques modernes avec **JUnit 5** et **AssertJ**.


##  Spécifications et Fonctionnalités Implémentées

L'application respecte les trois piliers fonctionnels requis par le cahier des charges :

1. **Gestion de l'Étagère (`BookShelf`) :**
   - Stockage interne dynamique via une structure `ArrayList`.
   - Ajout de livres optimisé à l'aide d'une méthode atomique acceptant des **arguments variables (varargs)**.
   - Encapsulation stricte : la méthode `books()` renvoie une **vue immuable** (`Collections.unmodifiableList`) pour empêcher toute altération malveillante ou involontaire par le client.

2. **Tri Multi-Critères (`arrange`) :**
   - Tri lexicographique par défaut basé sur l'ordre naturel des titres (la classe `Book` implémente de manière stricte l'interface native **`Comparable<Book>`**).
   - Surcharge de méthode permettant d'injecter des critères personnalisés via un **`Comparator`** (ex: tri par date de publication, ordre inverse).
   - Isolation des données : utilisation des flux **Streams Java** pour garantir que le tri ne détruit jamais l'ordre d'insertion de la collection sous-jacente.

3. **Regroupement Dynamique et Générique (`groupBy`) :**
   - Regroupement initial par année de publication à l'aide du collecteur `Collectors.groupingBy`.
   - Refactorisation vers une architecture hautement flexible grâce à une **méthode générique** acceptant l'interface fonctionnelle `Function<Book, K>`, permettant au client de regrouper instantanément sa collection par auteur, par éditeur ou par titre.
