import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.WorldState
import org.yaml.snakeyaml.Yaml
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths


class Controller(val world: WorldState, view: View) {

    fun tryYaml() {
        try {
            val gson = Gson()
            val reader: Reader = Files.newBufferedReader(Paths.get("assets/users.json"))
            val users: User =
                Gson().fromJson(reader, object : TypeToken<User?>() {}.type)

            // print users
            users.forEach { println(it) }

            // close reader
            reader.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        val yaml = Yaml()
        val document = """
 - Hesperiidae
 - Papilionidae
 - Apatelodidae
 - Epiplemidae" 
 - 1
        """.trimIndent()
        val list = yaml.load<Any>(document) as List<*>
        println(list)

    }

    var counter: Int = 0
        get() {
            val ret = field
            field++
            return ret
        }


}

