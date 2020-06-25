package com.pbeagan.services.hello

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pbeagan.models.User
import java.nio.file.Files
import java.nio.file.Paths

class HelloRepository {
    fun getHello(): String = "Ktor & Koin"
    fun getDemoJson(): User? {
        try {
            val path = Paths.get("assets/users.json")
            val fromJson = Gson()
                .fromJson<User?>(
                    Files.newBufferedReader(path),
                    object : TypeToken<User?>() {}.type
                )

            Files.newBufferedReader(path).close()
            return fromJson
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
}