package com.pbeagan.writer

import com.pbeagan.actions.Action
import com.pbeagan.data.Direction
import com.pbeagan.data.Mob
import com.pbeagan.consolevision.TerminalColorStyle.Colors
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Black1
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Black2
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Blue
import com.pbeagan.consolevision.TerminalColorStyle.style

class WriteTarget(
    private val writer: WriterInternal,
    private val target: Array<out Mob>
) {

    private val horizontalRule = (0..20).joinToString("") { "-" }
    fun system(s: String, shouldIndent: Boolean = false) =
        write {
            s.formatChannel(
                "GAME:".style(colorForeground = Colors.Black, colorBackground = Colors.Yellow) + " ", shouldIndent
            )
        }

    fun door(direction: Direction, s: String, shouldIndent: Boolean = false) =
        write {
            s.formatChannel(
                "${direction.name.take(4)}:".style(
                    colorForeground = Colors.Black,
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
                    colorForeground = Colors.Black,
                    colorBackground = Colors.Red
                ) + " ", shouldIndent
            )
        }

    fun error(s: String, shouldIndent: Boolean = false) =
        write {
            s.formatChannel(
                "ERR :".style(colorForeground = Colors.Black, colorBackground = Colors.RedBright) + " ",
                shouldIndent
            )
        }

    fun move(s: String, shouldIndent: Boolean = false) =
        write { s.formatChannel("MOVE:".style(colorBackground = Black1) + " ", shouldIndent) }

    fun join(s: String, shouldIndent: Boolean = false) =
        write { s.formatChannel("JOIN:".style(colorBackground = Colors.Magenta) + " ", shouldIndent) }

    fun pending(a: Action, shouldIndent: Boolean = false) =
        write {
            a::class.java.simpleName.formatChannel(
                "PEND:".style(
                    colorForeground = Colors.Black,
                    colorBackground = Colors.White
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