import com.pbeagan.util.safeLet
import com.pbeagan.writer.Colors
import com.pbeagan.writer.ESC
import com.pbeagan.writer.SGR
import com.pbeagan.writer.style
import io.ktor.application.call
import io.ktor.response.respondBytes
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.awt.Image
import java.awt.Image.SCALE_AREA_AVERAGING
import java.awt.Image.SCALE_DEFAULT
import java.awt.Image.SCALE_FAST
import java.awt.Image.SCALE_REPLICATE
import java.awt.Image.SCALE_SMOOTH
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main2(args: Array<String>) {
    /* Returns a bytestring of the value passed at the URL
        e.g. "curl http://localhost:8000/yolo" prints "yolo " to the
        console in red letters and white background. */
    val server = embeddedServer(Netty, port = 8000) {
        routing {
            get("/{string}") {
                val string = call.parameters["string"] + "\n"
                call.respondBytes(string.style(Colors.Red, Colors.White).toByteArray())
            }
        }
    }
    server.start(wait = true)
}

fun colorIntToARGB(color: Int): ARGB {
    val a = color shr 24 and 255
    val r = color shr 16 and 255
    val g = color shr 8 and 255
    val b = color and 255
    return ARGB(a, r, g, b)
}

data class ARGB(val a: Int, val r: Int, val g: Int, val b: Int)

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

fun imagePrinter() {
    var read = ImageIO.read(File("/Users/pbeagan/Downloads/charizard.png"))
    printImage(read)
    read = ImageIO.read(File("/Users/pbeagan/Downloads/scene.png"))
    listOf(
        SCALE_DEFAULT,
        SCALE_FAST,
        SCALE_SMOOTH,
        SCALE_REPLICATE,
        SCALE_AREA_AVERAGING
    ).forEach {
        val width = read.width
        val height = read.height
        val aspectRatio = width / height
        val maxDimen = 50
        read.getScaledInstance(maxDimen * aspectRatio, maxDimen, it)
            .convertToBufferedImage()
            ?.also { printImage(it) }
    }
}

private fun printImage(read: BufferedImage) {
    (read.minY until read.height).forEach { y ->
        (read.minX until read.width).forEach { x ->
            val readColor = read.getRGB(x, y)
            val (a, r, g, b) = colorIntToARGB(readColor)
            val colorBackground = if (a == 0) Colors.Custom(g = 255) else Colors.Custom(r, g, b)
            " ".style(colorBackground = colorBackground).also { print(it + it) }
        }
        println()
    }
}

fun main(args: Array<String>) {
    imagePrinter()
    (0..255).forEach {
        println("${ESC}[38;2;$it;$it;${it}m test" + " ".style(colorBackground = Colors.Custom(it, it, it)))
    }
    (50..100).forEach { y ->
        (100 downTo 50).forEach { x ->
            " ".style(colorBackground = Colors.Custom(x, y, x)).also { print(it) }
        }
        println()
    }
    Colors::class.sealedSubclasses.forEach {
        safeLet(it.simpleName, it.objectInstance) { name, color ->
            println(name.style(color) + "test")
        }
    }
//    Colors.values().forEach { println("bold" + it.name.style(it, Colors.Default, SGR.BOLD) + "test") }
//    Colors.values().forEach {
//        (it.name.style(colorBackground = it) + "test").also { println(it) }
//    }
//    Colors.values().forEach { (it.name.style(colorBackground = it, sgr = SGR.BOLD) + "test").also { println(it) } }
//    Colors.values().forEach { (it.name.style(colorBackground = it, sgr = SGR.ITALIC) + "test").also { println(it) } }
    println("Todd wanted a ${"blue".style(Colors.Blue)} car")
    println("Todd ${"wanted".style(sgr = SGR.ITALIC)} a ${"redOnGreen".style(Colors.Red, Colors.Green, SGR.BOLD)} car")
    SGR.values().forEach {
        println(it.name.style(sgr = it))
    }
    (0..255).forEach { "$it ".style(Colors.CustomPreset(it)).also { print(it) } }
}

