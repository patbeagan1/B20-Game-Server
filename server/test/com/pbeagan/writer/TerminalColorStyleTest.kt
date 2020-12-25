package com.pbeagan.writer

import com.pbeagan.util.safeLet
import com.pbeagan.writer.TerminalColorStyle.Colors.Blue
import com.pbeagan.writer.TerminalColorStyle.Colors.Custom
import com.pbeagan.writer.TerminalColorStyle.Colors.CustomPreset
import com.pbeagan.writer.TerminalColorStyle.Colors.Green
import com.pbeagan.writer.TerminalColorStyle.Colors.Red
import com.pbeagan.writer.TerminalColorStyle.ESC
import com.pbeagan.writer.TerminalColorStyle.SGR
import com.pbeagan.writer.TerminalColorStyle.style
import org.junit.Test

internal class TerminalColorStyleTest {

//    Colors.values().forEach { println("bold" + it.name.style(it, Colors.Default, SGR.BOLD) + "test") }
//    Colors.values().forEach {
//        (it.name.style(colorBackground = it) + "test").also { println(it) }
//    }
//    Colors.values().forEach { (it.name.style(colorBackground = it, sgr = SGR.BOLD) + "test").also { println(it) } }
//    Colors.values().forEach { (it.name.style(colorBackground = it, sgr = SGR.ITALIC) + "test").also { println(it) } }

    @Test
    fun demoSGRValues() {
        SGR.values().forEach {
            println(it.name.style(sgr = it))
        }
    }

    @Test
    fun demoPresetColors() {
        (0..255).forEach { "$it ".style(CustomPreset(it)).also { print(it) } }
    }

    @Test
    fun demoInlineUsage() {
        println("Todd wanted a ${"blue".style(Blue)} car")
        println(
            "Todd ${"wanted".style(sgr = SGR.ITALIC)} a ${"redOnGreen".style(
                Red,
                Green,
                SGR.BOLD
            )} car"
        )
    }

    @Test
    fun demoNamedColors() {
        TerminalColorStyle.Colors::class.sealedSubclasses.forEach {
            safeLet(it.simpleName, it.objectInstance) { name, color ->
                println(name.style(color) + "test")
            }
        }
    }

    @Test
    fun testColorBlending() {
        (50..100).forEach { y ->
            (100 downTo 50).forEach { x ->
                " ".style(colorBackground = Custom(x, y, x)).also { print(it) }
            }
            println()
        }
    }

    @Test
    fun testGreyscaleColors() {
        (0..255).forEach {
            println("${ESC}[38;2;$it;$it;${it}m test" + " ".style(colorBackground = Custom(it, it, it)))
        }
    }

}