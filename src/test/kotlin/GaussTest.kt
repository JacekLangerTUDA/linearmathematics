// ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

internal class GaussTest {

    @Test
    fun solve() {
        var matrix = arrayOf(doubleArrayOf(2.0, 4.0), doubleArrayOf(2.0, 3.0))
        var vector = doubleArrayOf(5.0, 6.0)
        var expected = doubleArrayOf(4.5, -1.0)
        var actual = Gauss(matrix, vector)
        var a = actual.solve()
        assertContentEquals(expected, a)
    }

    @Test
    fun invertMatrix() {
    }
}
