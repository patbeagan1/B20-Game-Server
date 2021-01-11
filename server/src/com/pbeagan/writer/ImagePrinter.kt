package com.pbeagan.writer

import com.pbeagan.writer.ImagePrinter.CompressionStyle.DOTS
import com.pbeagan.writer.ImagePrinter.CompressionStyle.UP_DOWN
import com.pbeagan.writer.TerminalColorStyle.Colors
import com.pbeagan.writer.TerminalColorStyle.argbToColorInt
import com.pbeagan.writer.TerminalColorStyle.colorIntToARGB
import com.pbeagan.writer.TerminalColorStyle.style
import java.awt.Image
import java.awt.image.BufferedImage

class ImagePrinter {
    enum class CompressionStyle {
        UP_DOWN, DOTS
    }

    fun printImageCompressed(read: BufferedImage, compressionStyle: CompressionStyle = UP_DOWN) {
        (read.minY until read.height).chunked(2).forEach { y ->
            (read.minX until read.width).forEach { x ->
                val (a, r, g, b) = read.getRGB(x, y[0]).colorIntToARGB()
                val (a1, r1, g1, b1) = read.getRGB(x, y[1]).colorIntToARGB()
                when (compressionStyle) {
                    UP_DOWN -> "▄"
                    DOTS -> "▓"
                }.style(
                    colorBackground = if (a == 0) Colors.Custom(g = 255) else Colors.Custom(r, g, b),
                    colorForeground = if (a1 == 0) Colors.Custom(g = 255) else Colors.Custom(r1, g1, b1)
                ).also { print(it) }
            }
            println()
        }
    }

    fun printImageReducedPalette(read: BufferedImage) {
        (read.minY until read.height).chunked(2).forEach { y ->
            (read.minX until read.width).forEach { x ->
                val a = read.getRGB(x, y[0]).colorIntToARGB().argbToColorInt(false)
                val b = read.getRGB(x, y[1]).colorIntToARGB().argbToColorInt(false)
                "▄".style(
                    colorBackground = Color256.reduceColor16(a).let { Colors.CustomPreset(it ) },
                    colorForeground = Color256.reduceColor16(b).let { Colors.CustomPreset(it ) }
                ).also { print(it) }
            }
            println()
        }
    }

    fun printImage(read: BufferedImage) {
        (read.minY until read.height).forEach { y ->
            (read.minX until read.width).forEach { x ->
                val (a, r, g, b) = read.getRGB(x, y).colorIntToARGB()
                "  ".style(
                    colorBackground = if (a == 0) {
                        Colors.Custom(g = 255)
                    } else {
                        Colors.Custom(r, g, b)
                    }
                ).also { print(it) }
            }
            println()
        }
    }
}

fun Image.convertToBufferedImage(): BufferedImage? {
    if (this is BufferedImage) {
        return this
    }

    // Create a buffered image with transparency
    val bi = BufferedImage(
        getWidth(null), getHeight(null),
        BufferedImage.TYPE_INT_ARGB
    )
    val graphics2D = bi.createGraphics()
    graphics2D.drawImage(this, 0, 0, null)
    graphics2D.dispose()
    return bi
}
