package com.pbeagan.writer

import com.pbeagan.consolevision.ImagePrinter
import com.pbeagan.consolevision.convertToBufferedImage
import org.junit.Before
import org.junit.Test
import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

internal class ImagePrinterTest {
    lateinit var imagePrinter: ImagePrinter

    @Before
    fun setup() {
        imagePrinter = ImagePrinter()
    }

    @Test
    fun `charizard fullsize`() {
        imagePrinter.printImage(ImageIO.read(File("/Users/pbeagan/Downloads/charizard.png")))
    }

    @Test
    fun `charizard compressed`() {
        imagePrinter.printImageCompressed(ImageIO.read(File("/Users/pbeagan/Downloads/charizard.png")))
    }

    @Test
    fun `charizard compressed dots`() {
        imagePrinter.printImageCompressed(
            ImageIO.read(File("/Users/pbeagan/Downloads/charizard.png")),
            ImagePrinter.CompressionStyle.DOTS
        )
    }

    @Test
    fun `sizedown sampling comparison`() {
        val read = ImageIO.read(File("/Users/pbeagan/Downloads/lenna.png"))
        listOf(
            Image.SCALE_DEFAULT,
            Image.SCALE_FAST,
            Image.SCALE_SMOOTH,
            Image.SCALE_REPLICATE,
            Image.SCALE_AREA_AVERAGING
        ).forEach {
            val width = read.width
            val height = read.height
            val aspectRatio = width.toDouble() / height.toDouble()
            val maxDimen = 100
            read.getScaledInstance((maxDimen * aspectRatio).toInt(), maxDimen, it)
                .convertToBufferedImage()
                ?.also { bufferedImage -> imagePrinter.printImageCompressed(bufferedImage, ImagePrinter.CompressionStyle.DOTS) }
        }
    }
}