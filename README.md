# Linear Mathematics Library

by Jacek Langer

[Hier](dokumentation/Dokumentation.pdf) gehts zur vollständigen Dokumentation.

## Hintergrund

Bei LGS handelt es sich um ein Schulprojekt, zum Lösen von Matrizen.
Dabei findet der Gauß-Algorithmus Verwendung.
Bei der Implementierung handelt es sich um eine Klassenbibliothek die eine Statische Methode
bereitstellt.
Es wird ein Argument erwartet und ein weiteres ist Optional zu Übergeben.
Wird nur ein Argument übergeben wird aus der Übergebenen Matrix die Inverse Matrix gebildet.
Wird ein Vector als zweites Argument übergeben wird die Angegebene Matrix anhand des Lösungsvektors
gelöst.

## Anwendung

Diese Klassenbibliothek stellt eine Klasse bereit.
Bei der initialisierung der Klasse wird eine Mehrdimensionales Array von Double Werten erwartet.
Die angabe eines Lösungsvektors ist optional.

Wird kein Lösungsvektor angegeben kann die Matrix nicht gelöst werden, es steht lediglich die
Methode bereit die Inverse der Matrix zu bilden.
Die Matrix und der Lösungsvektor können nachträglich geändert werden bzw. an die solve Methode
übergeben werden.

Methoden:

    - solve
    - Invert

### solve

Löst die Matrix mit Hilfe des angegebenen Vektors.
Das Ergebnis dieser Operation ist ein Vektor.

_Matrix Lösen, vollständige Instanzierung:_

````
val matrix = arrayOf(doubleArrayOf(2.0, 4.0), doubleArrayOf(2.0, 3.0))

val vector = doubleArrayOf(5.0, 6.0)

val result = Gauss(matrix,vector).solve()
````

### solve (Parameterisiert)

Diese Funktion erwartet zwei Parameter.
Eine Matrix und einen Vektor.
Die Matrix und der Lösungsvektor werden neu gesetzt.
Anschließend die Matrix gelöst.

_Matrix Lösen:_

````

val matrix = arrayOf(doubleArrayOf(2.0, 4.0), doubleArrayOf(2.0, 3.0))

val gauss = Gauss(matrix) val vector = doubleArrayOf(5.0, 6.0)

val result = gauss().solve(matrix,vector)

````

### Invert

Generiert die Inverse der Matrix.

_Inverse einer Matrix generieren:_

````

val matrix = arrayOf(doubleArrayOf(2.0, 4.0), doubleArrayOf(2.0, 3.0))

val vector = doubleArrayOf(5.0, 6.0)

val inverse = Gauss(matrix,vector).invert()

````

#### Abhängigkeiten

- JVM v16
- Kotlin v1.6.2
- Java v17
- Gradle v7.4.2
- JUnit v5.6.0
- GSON 2.9.0 (testing only)


