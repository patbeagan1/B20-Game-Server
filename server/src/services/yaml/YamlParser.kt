package services.yaml

import org.yaml.snakeyaml.Yaml
import java.nio.file.Files
import java.nio.file.Paths

class YamlParser {
    fun <T> parseYamlList(
        pathName: String,
        function: (LinkedHashMap<String, Any>) -> T
    ): List<T> = Yaml()
        .load<List<LinkedHashMap<String, Any>>>(
            Files.newBufferedReader(
                Paths.get(pathName)
            )
        ).map(function)

    fun <T> parseYaml(
        pathName: String,
        function: (LinkedHashMap<String, Any>) -> T
    ): T = Yaml()
        .load<LinkedHashMap<String, Any>>(
            Files.newBufferedReader(
                Paths.get(pathName)
            )
        ).let(function)
}