package utils


import java.nio.file.Files
import java.nio.file.Paths

object FileHelper {
    private val fileName = "token.txt"

    fun saveToken(token: String) {
        Files.write(Paths.get(fileName), token.toByteArray())
    }

    fun readToken(): String {
        return String(Files.readAllBytes(Paths.get(fileName)))
    }
}
