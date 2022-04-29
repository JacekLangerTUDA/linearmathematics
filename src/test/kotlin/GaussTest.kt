// ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.absoluteValue
import kotlin.random.Random
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
        // TODO(Jack): write test
    }

    //    @Test
    fun testGenerateRandomTestData() {
        var tests: Array<Testdata> = Array(100) {
            var height = Random.nextInt(15)
            var width = Random.nextInt(height - 1, height + 1).absoluteValue

            var matrix = Array(if (height > 0) height else 0) { h ->
                IntArray(width) { w ->
                    Random.nextInt(-100, 100)
                }
            }
            var result: DoubleArray?
            var inverse: Array<DoubleArray>?

            var solvingVector =
                IntArray(
                    Random.nextInt(
                        height - 1,
                        height + 1
                    ).absoluteValue
                ) { i -> Random.nextInt(-100, 100) }

            var solving = DoubleArray(solvingVector.size) { i -> solvingVector[i].toDouble() }
            var mat =
                Array<DoubleArray>(matrix.size) { h -> DoubleArray(matrix[h].size) { w -> matrix[h][w].toDouble() } }
            var gauss = Gauss(mat, solving)

            // we don't care what exception is thrown at this point. if there is any exception thrown we assume it's not solvable.
            try {
                result = gauss.solve()
            } catch (invalid: Exception) {
                result = DoubleArray(0)
            }
            try {
                inverse = gauss.invertMatrix()
            } catch (ex: Exception) {
                inverse = Array<DoubleArray>(0) { i -> DoubleArray(0) }
            }
            Testdata(matrix, solvingVector, result, inverse)
        }

        TestDataProvider.saveToFile(tests)
    }

    @Test
    fun testDataFromFile() {
        var json = Files.readString(Path.of("src/test/resources/testdata.json"))

        for (data in TestDataProvider.findAllData()!!) {

            var solving =
                DoubleArray(data.solvingVector.size) { i -> data.solvingVector[i].toDouble() }
            var mat =
                Array<DoubleArray>(data.matrix.size) { h -> DoubleArray(data.matrix[h].size) { w -> data.matrix[h][w].toDouble() } }

            var gauss = Gauss(mat, solving)

            if (data.resultVector!!.isNotEmpty()) {
                var res = data.resultVector
                var sol = gauss.solve()
                assertContentEquals(res, sol)
            } else {
                assertThrows<Exception> { gauss.solve() }
            }
        }
    }
}
