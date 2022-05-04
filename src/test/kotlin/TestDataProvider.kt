import com.google.gson.Gson
import java.io.IOException
import java.nio.file.Path

class TestDataProvider {

    companion object {

        private val path: Path = Path.of("src/test/resources/testdata.json")

        @Throws(IOException::class)
        fun findAllData(path: Path = this.path): Array<Testdata>? {
            val json = path.toFile().readText()
            val gson = Gson()
            return gson.fromJson(json, Array<Testdata>::class.java)
        }

        fun saveToFile(data: Array<Testdata>) {

            val json = Gson().toJson(data)
            path.toFile().writeText(json)
        }

        fun findFirstEntry(): Testdata {
            return findAllData()!![0]
        }
    }
}
