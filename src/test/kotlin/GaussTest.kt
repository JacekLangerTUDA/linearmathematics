// ktlint-disable no-wildcard-imports
import exceptions.InvalidOperationException
import exceptions.UnsolvableMatrixException
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Path
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GaussTest {

    private val standard = Path.of("src/test/resources/testdata_standard.json")

    @Test
    fun solve() {
        val matrix = arrayOf(doubleArrayOf(2.0, 4.0), doubleArrayOf(2.0, 3.0))
        val vector = doubleArrayOf(5.0, 6.0)
        val expected = doubleArrayOf(4.5, -1.0)
        val actual = Gauss(matrix, vector)
        val a = actual.solve()
        assertContentEquals(expected, a)
    }

    //    @Test
    /**
     * Function to generate test data this can be used to test weather the program output is consistent..
     */
    fun testGenerateRandomTestData() {
        val tests: Array<Testdata> = Array(100) {
            val height = Random.nextInt(15)
            val width = Random.nextInt(height - 1, height + 1).absoluteValue

            val matrix = Array(if (height > 0) height else 0) { h ->
                IntArray(width) { w ->
                    Random.nextInt(-100, 100)
                }
            }
            var result: DoubleArray?
            var inverse: Array<DoubleArray>?

            val solvingVector =
                IntArray(
                    Random.nextInt(
                        height - 1,
                        height + 1
                    ).absoluteValue
                ) { i -> Random.nextInt(-100, 100) }

            val solving = DoubleArray(solvingVector.size) { i -> solvingVector[i].toDouble() }
            val mat =
                Array<DoubleArray>(matrix.size) { h -> DoubleArray(matrix[h].size) { w -> matrix[h][w].toDouble() } }
            val gauss = Gauss(mat, solving)

            // we don't care what exception is thrown at this point. if there is any exception thrown we assume it's not solvable.
            result = try {
                gauss.solve()
            } catch (invalid: Exception) {
                DoubleArray(0)
            }
            inverse = try {
                gauss.invertMatrix()
            } catch (ex: Exception) {
                Array<DoubleArray>(0) { i -> DoubleArray(0) }
            }
            Testdata(matrix, solvingVector, result, inverse)
        }

        TestDataProvider.saveToFile(tests)
    }

    /**
     * test using data from file.
     * This can be used to thoroughly test the algorithm as big data sets can be used. Depending on the Dataset this method is slow.
     */
    @Test
    @Tag("slow")
    fun testDataFromFile() {

        // TODO test inverse
        // test basic data
        for (data in TestDataProvider.findAllData(standard)!!) {
            val solving =
                DoubleArray(data.solvingVector.size) { i -> data.solvingVector[i].toDouble() }
            val mat =
                Array<DoubleArray>(data.matrix.size) { h -> DoubleArray(data.matrix[h].size) { w -> data.matrix[h][w].toDouble() } }

            val gauss = Gauss(mat, solving)

            if (data.resultVector!!.isNotEmpty()) {
                val res = data.resultVector
                val sol = gauss.solve()
                assertContentEquals(res, sol)
            } else {
                assertThrows<Exception> { gauss.solve() }
            }
        }
        // test computed data
        for (data in TestDataProvider.findAllData()!!) {

            val solving =
                DoubleArray(data.solvingVector.size) { i -> data.solvingVector[i].toDouble() }
            val mat =
                Array<DoubleArray>(data.matrix.size) { h -> DoubleArray(data.matrix[h].size) { w -> data.matrix[h][w].toDouble() } }

            val gauss = Gauss(mat, solving)

            if (data.resultVector!!.isNotEmpty()) {
                val res = data.resultVector
                val sol = gauss.solve()
                assertContentEquals(res, sol)
            } else {
                assertThrows<Exception> { gauss.solve() }
            }
        }
    }

    /**
     * Test if inverse matrix works as expected.
     */
    @Test
    fun testInverse() {
        val data = TestDataProvider.findAllData(standard)
        for (expected in data!!) {

            try {
                val actual = Gauss(expected.matrix).invertMatrix()
                assertContentEquals(expected.inverse, actual)
            } catch (ex: Exception) {
                assertTrue(ex is UnsolvableMatrixException || ex is InvalidOperationException)
            }
        }
    }

    /**
     * Tests matrix multiplication from left.
     */
    @Test
    fun multiplyFromLeft() {
        val testdata = TestDataProvider.findFirstEntry()
        val invert = Gauss(testdata.matrix).invertMatrix()
        val unificationMatrix = UnificationMatrix(testdata.matrix.size)
        assertContentEquals(unificationMatrix.data, testdata.matrix.toDoubleArray() * invert)
    }

    /**
     * Tests matrix multiplication from right.
     */
    @Test
    fun multiplyFromRight() {

        val testdata = TestDataProvider.findFirstEntry()
        val invert = Gauss(testdata.matrix).invertMatrix()
        val unificationMatrix = UnificationMatrix(testdata.matrix.size)
        assertContentEquals(unificationMatrix.data, invert * testdata.matrix.toDoubleArray())
    }

    /**
     * Test whether multiplication from left works as expected.
     */
    @Test
    fun multiplicationTest() {
        val first = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(2.0, 4.0, 3.0),
            arrayOf(1.0, -0.5, -2.0)
        )

        val second = arrayOf(
            arrayOf(1.0, 3.0, 5.0),
            arrayOf(1.0, -2.0, 2.0),
            arrayOf(1.0, -1.0, 2.0)
        )

        val expected = arrayOf(
            arrayOf(6.0, -4.0, 15.0),
            arrayOf(9.0, -5.0, 24.0),
            arrayOf(-1.5, 6.0, 0.0)
        )

        val result: Array<Array<Double>> = first * second

        for (i in result.indices) {
            for (v in result[i].indices) {
                assertEquals(expected[i][v], result[i][v], "does not equal at index[$i,$v]")
            }
        }
    }
}

private operator fun Array<Array<Double>>.times(second: Array<Array<Double>>): Array<Array<Double>> {
    val ar = Array(this.size, { i ->
        DoubleArray(this[i].size) { w ->
            this[i][w]
        }
    })

    val se = Array(second.size, { i ->
        DoubleArray(second[i].size) { w ->
            second[i][w]
        }
    })

    val res = ar * se
    return Array(res.size) { h -> Array<Double>(res[h].size) { w -> res[h][w] } }
}
