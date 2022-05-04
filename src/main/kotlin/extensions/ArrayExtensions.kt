package extensions

class ArrayExtensions {

    companion object {

        /**
         * This extends the array.
         * @param alpha the multiplier
         * @return double array with all values multiplied by alpha.
         */
        private operator fun DoubleArray.times(alpha: Double): DoubleArray {
            val temp = this.copyOf()
            for (i in 0 until temp.size) {
                temp[i] *= alpha
            }
            return temp
        }

        private operator fun DoubleArray.plus(elements: DoubleArray): DoubleArray {

            for (i in 0 until this.size)
                this[i] += elements[i]
            return this
        }

        private operator fun Array<DoubleArray>.times(matrix: Array<DoubleArray>): Array<DoubleArray>? {

            val temp = this.copyOf()
            for (i in 0 until this.size) {
                for (h in 0 until this.size) {
                    for (w in 0 until this[h].size) {
                        temp[i][h] += matrix[w][h] * matrix[h][w]
                    }
                }
            }
            return temp
        }
    }
}
