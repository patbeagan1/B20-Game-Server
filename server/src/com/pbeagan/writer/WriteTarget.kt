package com.pbeagan.writer

import com.pbeagan.actions.Action
import com.pbeagan.data.Mob

class WriteTarget(
    private val writer: WriterInternal,
    private val target: Array<out Mob>
) {

    private val horizontalRule = (0..20).joinToString("") { "-" }
    fun system(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("GAME: ", shouldIndent) }
    fun door(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("DOOR: ", shouldIndent) }
    fun info(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("INFO: ", shouldIndent) }
    fun localMap(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("MAP : ", shouldIndent) }
    fun dead(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("DEAD: ", shouldIndent) }
    fun combat(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("CMBT: ", shouldIndent) }
    fun error(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("ERR : ", shouldIndent) }
    fun move(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("MOVE: ", shouldIndent) }
    fun join(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("JOIN: ", shouldIndent) }
    fun pending(a: Action, shouldIndent: Boolean = false) =
        write { a::class.java.simpleName.formatChannel("PEND: Pending Action: ", shouldIndent) }

    fun horizontalRule() = write { horizontalRule }
    fun turnStart(name: String) {
        write { target ->
            """
          $horizontalRule
          Turn Start: ${if (target.name == name) "$name (you)" else name}

      """.trimIndent()
        }
    }

    private fun String.formatChannel(prefix: String, indent: Boolean) = "\n${this}"
        .replace("\n", "\n$prefix${if (indent) "\t" else ""}")
        .trim()

    private fun write(s: (Mob) -> String) {
        target.forEach { writer.write(it, s) }
    }
}