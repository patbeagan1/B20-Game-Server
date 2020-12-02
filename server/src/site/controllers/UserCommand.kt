package site.controllers

import io.ktor.application.call
import io.ktor.response.respondText
import site.Command
import site.Pipeline

class UserCommand(
    val message: List<Pair<String, String>>,
    val name: String?
) : Command() {
    override fun execute(): Pipeline = {
        call.respondText("n: $name -- m: $message")
    }
}
