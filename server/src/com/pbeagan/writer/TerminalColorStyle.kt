package com.pbeagan.writer

/**
 * Terminal escape sequence
  */
const val ESC = "\u001B"

/**
 * Control Sequence Introducer
 */
const val CSI = "$ESC["

/**
 * Reset Initial State
 */
const val RIS = "${ESC}c"

sealed class Colors(val foreground: String, val background: String) {
    object Default : Colors("39", "49")
    object White : Colors("30", "40")
    object Black_1 : Colors("37", "47")
    object Black_2 : Colors("90", "100")
    object Black : Colors("97", "107")
    object Blue : Colors("34", "44")
    object BlueBright : Colors("94", "104")
    object Cyan : Colors("36", "46")
    object CyanBright : Colors("96", "106")
    object Green : Colors("32", "42")
    object GreenBright : Colors("92", "102")
    object Magenta : Colors("35", "45")
    object MagentaBright : Colors("95", "105")
    object Red : Colors("31", "41")
    object RedBright : Colors("91", "101")
    object Yellow : Colors("33", "43")
    object YellowBright : Colors("93", "103")
    class Custom(r: Int = 0, g: Int = 0, b: Int = 0) : Colors("38;2;$r;$g;$b", "48;2;$r;$g;$b")
    class CustomPreset(value: Int = 0) : Colors("38;5;$value", "48;5;$value")
}

// Select Graphic Rendition
enum class SGR(val value: Int) {
    RESET(0),
    BOLD(1),
    DIM (2),
    ITALIC(3),
    UNDERLINE(4),
    NORMAL(22),
    FRAMED(51),
    SUPERSCRIPT(73),
    SUBSCRIPT(74),
    BLINK(5);
}

fun String.style(
    colorForeground: Colors = Colors.Default,
    colorBackground: Colors = Colors.Default,
    sgr: SGR = SGR.NORMAL
): String {
    val startColor = "$CSI${sgr.value};${colorForeground.foreground};${colorBackground.background}m"
    val endColor = "$CSI${SGR.RESET.value};${Colors.Default.foreground};${Colors.Default.background}m"
    return startColor + this + endColor
}