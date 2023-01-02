package com.pbeagan.writer

import com.pbeagan.consolevision.TerminalColorStyle
import com.pbeagan.util.safeLet
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Blue
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Custom
import com.pbeagan.consolevision.TerminalColorStyle.Colors.CustomPreset
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Green
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Red
import com.pbeagan.consolevision.TerminalColorStyle.ESC
import com.pbeagan.consolevision.TerminalColorStyle.SGR
import com.pbeagan.consolevision.TerminalColorStyle.style
import org.junit.Test

internal class TerminalColorStyleTest {

    @Test
    fun demoSGRValues() {
        SGR::class.sealedSubclasses.forEach {
            println(it.simpleName?.style(sgr = it.objectInstance!!))
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
            "Todd ${"wanted".style(sgr = SGR.Italic)} a ${"redOnGreen".style(
                Red,
                Green,
                SGR.Bold
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
    fun demoInlineSGR() {
        SGR::class.sealedSubclasses.forEach {
            safeLet(it.simpleName, it.objectInstance) { name, sgr ->
                println("test${name.style(sgr = sgr)}test")
            }
        }
        println()
        println("1test${SGR.Underline.enableString()}2test${"3test".style(sgr = arrayOf(SGR.Bold, SGR.Framed))}4test")
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