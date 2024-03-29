import exceptions.InvalidOperationException
import exceptions.UnsolvableMatrixException

class Gauss(
    private var matrix: Array<DoubleArray>,
    private var solvingVector: DoubleArray?
) {

    /**
     * Creates a new Gauss object.
     */
    constructor(
        matrix: Array<IntArray>,
        solvingVector: DoubleArray?
    ) : this(matrix.toDoubleMatrix(), solvingVector)

    constructor(matrix: Array<IntArray>, solvingVector: IntArray?) : this(
        matrix.toDoubleMatrix(),
        solvingVector.toDoubleArray()
    )

    constructor(matrix: Array<IntArray>) : this(matrix.toDoubleMatrix(), null)

    /**
     * The result vector. this is a copy of the solving vector.
     */
    private val resultVector: DoubleArray =
        solvingVector?.copyOf() ?: DoubleArray(matrix.size)

    /**
     * Unification matrix of the same size as the matrix provided.
     */
    private val inverse = UnificationMatrix(matrix.size)

    /**
     * Weather the matrix was solved or not.
     */
    private var wasNotSolved: Boolean = true

    /**
     * Solves a matrix using the provided vector.
     * * @param matrix the matrix to solve
     * @param vector the solving vector
     * @return a result vector containing the result of the operation
     */
    fun solve(matrix: Array<IntArray>, vector: IntArray): DoubleArray {
        this.matrix =
            Array<DoubleArray>(matrix.size) { i -> DoubleArray(matrix[i].size) { v -> matrix[i][v].toDouble() } }
        this.solvingVector = DoubleArray(vector.size) { i -> vector[i].toDouble() }

        return solve()
    }

    /**
     * Sets new values for matrix and solving vector and returns the solved vector.
     */
    fun solve(matrix: Array<DoubleArray>, vector: DoubleArray): DoubleArray {

        this.matrix = matrix
        this.solvingVector = vector
        return solve()
    }

    /**
     * Solve the matrix using the solving vector provided.
     */
    @Throws(InvalidOperationException::class, UnsolvableMatrixException::class)
    fun solve(): DoubleArray {

        if (solvingVector == null)
            throw InvalidOperationException("This operation is not valid as no solving vector was provided.")

        if (matrix.size != resultVector.size || matrix.size != matrix[0].size) throw UnsolvableMatrixException(
            "The matrix is not solvable due to a mismatch between the matrix rank and result vector rank"
        )
        // create a deep copy of the matrix so we can keep the original matrix intact
        var copy =
            Array<DoubleArray>(matrix.size) { i -> DoubleArray(matrix[i].size) { v -> matrix[i][v] } }

        invertMatrix(copy)
        iterateDown()
        iterateUp()
        normalize()
        wasNotSolved = false

        return resultVector
    }

    fun invertMatrix(matrix: Array<DoubleArray>): UnificationMatrix {

        // iterate over the matrix downwards
        for (i in matrix.indices) {
            // adjust the values in the result vector
            var nextrow = i + 1
            for (h in nextrow until matrix.size) {
                val alpha = -1 / (matrix[i][i] / matrix[h][i])
                // first iteration only we don't need to change the result
                inverse[h] += inverse[i] * alpha
                matrix[h] += matrix[i] * alpha
            }
        }

        // iterate up
        for (i in matrix.size - 1 downTo 1) {
            val alpha = -1 / (matrix[i][i] / matrix[i - 1][i])
            for (h in i - 1 downTo 0) {
                matrix[h] += matrix[i] * alpha
                inverse[h] += inverse[i] * alpha
            }
        }

        // normalize matrix
        for (i in matrix.indices) {
            val alpha = 1 / matrix[i][i]
            matrix[i][i] *= alpha
            inverse[i] = inverse[i] * alpha
        }

        return inverse
    }

    /**
     * Normalize the matrix, a normalized matrix after all iterations should resemble an unification matrix.
     */
    private fun normalize() {
        for (i in matrix.indices) {
            val alpha = 1 / matrix[i][i]
            matrix[i][i] *= alpha
            resultVector[i] *= alpha
            inverse[i] = inverse[i] * alpha
        }
    }

    /**
     * Iterate backwards over the matrix.
     */
    private fun iterateUp() {

        for (i in matrix.size - 1 downTo 1) {
            val alpha = -1 / (matrix[i][i] / matrix[i - 1][i])

            for (h in i - 1 downTo 0) {
                if (solvingVector != null) {
                    resultVector[h] += resultVector[i] * alpha
                }
                matrix[h] += matrix[i] * alpha
            }
        }
    }

    /**
     * Iterate over the matrix. this will set all the values to the left to 0 while creating a step shape.
     */
    @Throws(InvalidOperationException::class, UnsolvableMatrixException::class)
    private fun iterateDown() {

        // iterate over the matrix
        for (i in 0 until matrix.size - 1) {
            // is the next row a multiple of the current row.
            if (matrix[i] / matrix[i + 1]) {
                throw UnsolvableMatrixException("This Matrix is not solvable as the Rank of the matrix and rank of the solving Vector do not match.")
            }

            // check if row and next row is preceded by 0 if yes throw exception
            if (matrix[i][i] == 0.0) {
                if (matrix[i + 1][i] == 0.0) {
                    throw UnsolvableMatrixException("This Matrix is not solvable as the Rank of the matrix and rank of the solving Vector do not match.")
                } else {
                    pivot(i, i + 1)
                }
            }

            // adjust the values in the result vector
            for (h in (i + 1) until matrix.size) {
                val alpha = -1 / (matrix[i][i] / matrix[h][i])
                // first iteration only we don't need to change the result

                if (solvingVector != null) {
                    resultVector[h] += resultVector[i] * alpha
                }

                matrix[h] += matrix[i] * alpha
            }
        }
    }

    /**
     * Swaps two rows
     */
    private fun pivot(current: Int, next: Int) {
        val currentVals = matrix[current]
        matrix[current] = matrix[next]
        matrix[next] = currentVals
    }
}

private fun IntArray?.toDoubleArray(): DoubleArray? {
    return DoubleArray(this!!.size) { i -> this[i].toDouble() }
}

private fun Array<IntArray>.toDoubleMatrix(): Array<DoubleArray> {

    return Array<DoubleArray>(this.size) { h -> DoubleArray(this[h].size) { w -> this[h][w].toDouble() } }
}

/**
 * Check whether a row is a multiple of another row.

 * @param doubles the other double array.
 * @return true if is multiple
 */
private operator fun DoubleArray.div(doubles: DoubleArray): Boolean {

    for (i in 0 until this.size) {
        val a = this[i]
        val b = doubles[i]
        if ((a % b).toInt() != 0)
            return false
    }
    return true
}

/**
 * This extends the array.
 * @param alpha the multiplier
 * @return double array with all values multiplied by alpha.
 */
operator fun DoubleArray.times(alpha: Double): DoubleArray {
    val temp = this.copyOf()
    for (i in temp.indices) {
        temp[i] *= alpha
    }
    return temp
}

operator fun DoubleArray.plus(elements: DoubleArray): DoubleArray {

    for (i in 0 until this.size)
        this[i] += elements[i]
    return this
}

operator fun Array<DoubleArray>.times(matrix: Array<DoubleArray>): Array<DoubleArray> {

    val temp = Array<DoubleArray>(matrix.size) { DoubleArray(matrix[it].size) }

    for (i in 0 until this.size) {
        for (h in 0 until this.size) {
            for (w in 0 until this[h].size) {
                temp[i][h] += this[i][w] * matrix[w][h]
            }
        }
    }
    return temp
}

/**
 * Extends [IntArray]. Convert to [DoubleArray].
 */
fun Array<IntArray>.toDoubleArray(): Array<DoubleArray> {
    return Array(this.size) { i -> DoubleArray(this[i].size) { j -> this[i][j].toDouble() } }
}
