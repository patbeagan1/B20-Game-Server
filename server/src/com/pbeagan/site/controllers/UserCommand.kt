package com.pbeagan.site.controllers

import io.ktor.application.call
import io.ktor.response.respondText
import com.pbeagan.site.Command
import com.pbeagan.site.Pipeline

class UserCommand(
    val message: List<Pair<String, String>>,
    val name: String?
) : Command() {
    override fun execute(): Pipeline = {
        call.respondText("n: $name -- m: $message")
    }
}
