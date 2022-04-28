import com.google.gson.Gson
import java.io.IOException
import java.nio.file.Path

class TestDataProvider {

    companion object {

        private val path: Path = Path.of("src/test/resources/testdata.json")

        @Throws(IOException::class)
        fun findAllData(): Array<Testdata>? {
            var json = path.toFile().readText()
            var gson = Gson()
            return gson.fromJson(json, Array<Testdata>::class.java)
        }

        fun saveToFile(data: Array<Testdata>) {

            var json = Gson().toJson(data)
            path.toFile().writeText(json)
        }
    }
}
