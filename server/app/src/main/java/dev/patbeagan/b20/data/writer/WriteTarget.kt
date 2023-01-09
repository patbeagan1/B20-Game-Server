package com.pbeagan.data.writer

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import dev.patbeagan.b20.domain.types.Direction
import dev.patbeagan.consolevision.ansi.AnsiColor
import dev.patbeagan.consolevision.ansi.AnsiColor.*
import dev.patbeagan.consolevision.style

class WriteTarget(
    private val writer: WriterInternal,
    private val target: List<Mob>,
) {

    private val horizontalRule = (0..20).joinToString("") { "-" }
    fun system(s: String, shouldIndent: Boolean = false) =
        write {
            s.formatChannel(
                "GAME:".style(
                    colorForeground = Black,
                    colorBackground = Yellow
                ) + " ", shouldIndent
            )
        }

    fun door(direction: Direction, s: String, shouldIndent: Boolean = false) =
        write {
            s.formatChannel(
                "${direction.name.take(4)}:".style(
                    colorForeground = Black,
                    colorBackground = Black2
                ) + " ", shouldIndent
            )
        }

    fun look(s: String, shouldIndent: Boolean = false) =
        write { s.formatChannel("LOOK:".style(colorBackground = Blue) + " ", shouldIndent) }


    fun info(s: String, shouldIndent: Boolean = false) =
        write { s.formatChannel("INFO:".style(colorBackground = Black1) + " ", shouldIndent) }

    fun localMap(s: String, shouldIndent: Boolean = false) =
        write { s.formatChannel("MAP :".style(colorBackground = Black1) + " ", shouldIndent) }

    fun dead(s: String, shouldIndent: Boolean = false) =
        write { s.formatChannel("DEAD:".style(colorBackground = Black1) + " ", shouldIndent) }

    fun combat(s: String, shouldIndent: Boolean = false) =
        write {
            s.formatChannel(
                "CMBT:".style(
                    colorForeground = Black,
                    colorBackground = Red
                ) + " ", shouldIndent
            )
        }

    fun error(s: String, shouldIndent: Boolean = false) =
        write {
            s.formatChannel(
                "ERR :".style(
                    colorForeground = Black,
                    colorBackground = RedBright
                ) + " ",
                shouldIndent
            )
        }

    fun move(s: String, shouldIndent: Boolean = false) =
        write { s.formatChannel("MOVE:".style(colorBackground = Black1) + " ", shouldIndent) }

    fun join(s: String, shouldIndent: Boolean = false) =
        write {
            s.formatChannel(
                "JOIN:".style(colorBackground = Magenta) + " ",
                shouldIndent
            )
        }

    fun pending(a: Action, shouldIndent: Boolean = false) =
        write {
            a::class.java.simpleName.formatChannel(
                "PEND:".style(
                    colorForeground = Black,
                    colorBackground = White
                ) + " " + " Pending Action: ", shouldIndent
            )
        }

    fun horizontalRule() = write { horizontalRule }
    fun turnStart(name: String) {
        write { target ->
            """
          $horizontalRule
          Turn Start: ${if (target.nameStyled == name) "$name (you)" else name}

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