class UnificationMatrix(size: Int) {

    val data = Array<DoubleArray>(size) { i ->
        DoubleArray(size) { w ->
            if (w == i) 1.0 else 0.0
        }
    }

    operator fun get(row: Int, col: Int): Double {
        return data[row][col]
    }

    operator fun get(row: Int): DoubleArray {
        return data[row]
    }

    operator fun set(index: Int, value: DoubleArray): DoubleArray {
        for (i in 0 until data.size)
            data[index][i] = value[i]
        return data[index]
    }
}
