package com.pbeagan.demo


/*******************************************************************************
 * Copyright (c) 2014, Art Clarke.  All rights reserved.
 *
 * This file is part of Humble-Video.
 *
 * Humble-Video is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Humble-Video is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Humble-Video.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
import com.pbeagan.writer.ImagePrinter
import com.pbeagan.writer.TerminalColorStyle.CURSOR_TO_START
import com.pbeagan.writer.TerminalColorStyle.RIS
import com.pbeagan.writer.convertToBufferedImage
import io.humble.video.Decoder
import io.humble.video.Demuxer
import io.humble.video.DemuxerStream
import io.humble.video.Global
import io.humble.video.Media
import io.humble.video.MediaDescriptor
import io.humble.video.MediaPacket
import io.humble.video.MediaPicture
import io.humble.video.Rational
import io.humble.video.awt.ImageFrame
import io.humble.video.awt.MediaPictureConverter
import io.humble.video.awt.MediaPictureConverterFactory
import org.apache.commons.cli.BasicParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import java.awt.image.BufferedImage
import java.io.IOException


/**
 * Opens a media file, finds the first video stream, and then plays it.
 * This is meant as a demonstration program to teach the use of the Humble API.
 *
 *
 * Concepts introduced:
 *
 *
 *  * MediaPicture: [MediaPicture] objects represent uncompressed video in Humble.
 *  * Timestamps: All [Media] objects in Humble have a timestamp, and this demonstration introduces the concept of having to worry about *when* to display information.
 *
 *
 *
 *
 * To run from maven, do:
 *
 * <pre>
 * mvn install exec:java -Dexec.mainClass="io.humble.video.demos.DecodeAndPlayVideo" -Dexec.args="filename.mp4"
</pre> *
 *
 * @author aclarke
 */
object DecodeAndPlayVideo {
    /**
     * Opens a file, and plays the video from it on a screen at the right rate.
     * @param filename The file or URL to play.
     */
    @Throws(InterruptedException::class, IOException::class)
    private fun playVideo(filename: String) {
        /*
     * Start by creating a container object, in this case a demuxer since
     * we are reading, to get video data from.
     */
        val demuxer: Demuxer = Demuxer.make()

        /*
     * Open the demuxer with the filename passed on.
     */demuxer.open(filename, null, false, true, null, null)

        /*
     * Query how many streams the call to open found
     */
        val numStreams: Int = demuxer.numStreams

        /*
     * Iterate through the streams to find the first video stream
     */
        var videoStreamId = -1
        var streamStartTime: Long = Global.NO_PTS
        var videoDecoder: Decoder? = null
        for (i in 0 until numStreams) {
            val stream: DemuxerStream = demuxer.getStream(i)
            streamStartTime = stream.startTime
            val decoder: Decoder = stream.decoder
            if (decoder != null && decoder.codecType === MediaDescriptor.Type.MEDIA_VIDEO) {
                videoStreamId = i
                videoDecoder = decoder
                // stop at the first one.
                break
            }
        }
        if (videoStreamId == -1) throw RuntimeException("could not find video stream in container: $filename")

        /*
     * Now we have found the audio stream in this file.  Let's open up our decoder so it can
     * do work.
     */
        videoDecoder!!
        videoDecoder.open(null, null)
        val picture: MediaPicture = MediaPicture.make(
            videoDecoder.width,
            videoDecoder.height,
            videoDecoder.pixelFormat
        )

        /** A converter object we'll use to convert the picture in the video to a BGR_24 format that Java Swing
         * can work with. You can still access the data directly in the MediaPicture if you prefer, but this
         * abstracts away from this demo most of that byte-conversion work. Go read the source code for the
         * converters if you're a glutton for punishment.
         */
        val converter: MediaPictureConverter = MediaPictureConverterFactory.createConverter(
            MediaPictureConverterFactory.HUMBLE_BGR_24,
            picture)
        var image: BufferedImage? = null
        /**
         * This is the Window we will display in. See the code for this if you're curious, but to keep this demo clean
         * we're 'simplifying' Java AWT UI updating code. This method just creates a single window on the UI thread, and blocks
         * until it is displayed.
         */
        /**
         * This is the Window we will display in. See the code for this if you're curious, but to keep this demo clean
         * we're 'simplifying' Java AWT UI updating code. This method just creates a single window on the UI thread, and blocks
         * until it is displayed.
         */
        val window: ImageFrame = ImageFrame.make()
            ?: throw RuntimeException("Attempting this demo on a headless machine, and that will not work. Sad day for you.")

        /**
         * Media playback, like comedy, is all about timing. Here we're going to introduce **very very basic**
         * timing. This code is deliberately kept simple (i.e. doesn't worry about A/V drift, garbage collection pause time, etc.)
         * because that will quickly make things more complicated.
         *
         * But the basic idea is there are two clocks:
         *
         *  * Player Clock: The time that the player sees (relative to the system clock).
         *  * Stream Clock: Each stream has its own clock, and the ticks are measured in units of time-bases
         *
         *
         * And we need to convert between the two units of time. Each MediaPicture and MediaAudio object have associated
         * time stamps, and much of the complexity in video players goes into making sure the right picture (or sound) is
         * seen (or heard) at the right time. This is actually very tricky and many folks get it wrong -- watch enough
         * Netflix and you'll see what I mean -- audio and video slightly out of sync. But for this demo, we're erring for
         * 'simplicity' of code, not correctness. It is beyond the scope of this demo to make a full fledged video player.
         */

        // Calculate the time BEFORE we start playing.
        val systemStartTime = System.nanoTime()
        // Set units for the system time, which because we used System.nanoTime will be in nanoseconds.
        val systemTimeBase: Rational = Rational.make(1, 1000000000)
        // All the MediaPicture objects decoded from the videoDecoder will share this timebase.
        val streamTimebase: Rational = videoDecoder.timeBase

        /**
         * Now, we start walking through the container looking at each packet. This
         * is a decoding loop, and as you work with Humble you'll write a lot
         * of these.
         *
         * Notice how in this loop we reuse all of our objects to avoid
         * reallocating them. Each call to Humble resets objects to avoid
         * unnecessary reallocation.
         */
        val packet: MediaPacket = MediaPacket.make()
        while (demuxer.read(packet) >= 0) {
            /**
             * Now we have a packet, let's see if it belongs to our video stream
             */
            if (packet.streamIndex == videoStreamId) {
                /**
                 * A packet can actually contain multiple sets of samples (or frames of samples
                 * in decoding speak).  So, we may need to call decode  multiple
                 * times at different offsets in the packet's data.  We capture that here.
                 */
                var offset = 0
                var bytesRead = 0
                do {
                    bytesRead += videoDecoder.decode(picture, packet, offset)
                    if (picture.isComplete) {
                        image = displayVideoAtCorrectTime(
                            streamStartTime,
                            picture,
                            converter,
                            image,
                            window,
                            systemStartTime,
                            systemTimeBase,
                            streamTimebase
                        )
                    }
                    offset += bytesRead
                } while (offset < packet.size)
            }
        }

        // Some video decoders (especially advanced ones) will cache
        // video data before they begin decoding, so when you are done you need
        // to flush them. The convention to flush Encoders or Decoders in Humble Video
        // is to keep passing in null until incomplete samples or packets are returned.
        do {
            videoDecoder.decode(picture, null, 0)
            if (picture.isComplete) {
                image = displayVideoAtCorrectTime(
                    streamStartTime,
                    picture,
                    converter,
                    image,
                    window,
                    systemStartTime,
                    systemTimeBase,
                    streamTimebase
                )
            }
        } while (picture.isComplete)

        // It is good practice to close demuxers when you're done to free
        // up file handles. Humble will EVENTUALLY detect if nothing else
        // references this demuxer and close it then, but get in the habit
        // of cleaning up after yourself, and your future girlfriend/boyfriend
        // will appreciate it.
        demuxer.close()

        // similar with the demuxer, for the windowing system, clean up after yourself.
        window.dispose()
    }

    private val imagePrinter = ImagePrinter()

    /**
     * Takes the video picture and displays it at the right time.
     */
    @Throws(InterruptedException::class)
    private fun displayVideoAtCorrectTime(
        streamStartTime: Long,
        picture: MediaPicture, converter: MediaPictureConverter,
        image: BufferedImage?, window: ImageFrame, systemStartTime: Long,
        systemTimeBase: Rational, streamTimebase: Rational
    ): BufferedImage? {
        var image = image
        var streamTimestamp: Long = picture.timeStamp
        // convert streamTimestamp into system units (i.e. nano-seconds)
        streamTimestamp = systemTimeBase.rescale(streamTimestamp - streamStartTime, streamTimebase)
        // get the current clock time, with our most accurate clock
        var systemTimestamp = System.nanoTime()
        // loop in a sleeping loop until we're within 1 ms of the time for that video frame.
        // a real video player needs to be much more sophisticated than this.
//        while (streamTimestamp > systemTimestamp - systemStartTime + 1000000) {
            Thread.sleep(1000/20)
//            systemTimestamp = System.nanoTime()
//        }
        // finally, convert the image from Humble format into Java images.
        image = converter.toImage(image, picture)
        // And ask the UI thread to repaint with the new image.
        window.setImage(image)
        val width = image.width
        val height = image.height
        val aspectRatio = width.toDouble() / height.toDouble()
        val maxDimen = 100

        print(CURSOR_TO_START)
        image.getScaledInstance((maxDimen * aspectRatio).toInt(), maxDimen, java.awt.Image.SCALE_DEFAULT)
            .convertToBufferedImage()
            ?.also { bufferedImage ->
                imagePrinter.printImageReducedPalette(bufferedImage)
            }

        return image
    }

    /**
     * Takes a media container (file) as the first argument, opens it,
     * opens up a window and plays back the video.
     *
     * @param args Must contain one string which represents a filename
     * @throws IOException
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val options = Options()
        options.addOption("h", "help", false, "displays help")
        options.addOption("v", "version", false, "version of this library")
        val parser: CommandLineParser = BasicParser()
        try {
            val cmd: CommandLine = parser.parse(options, args)
            when {
                cmd.hasOption("version") -> {
                    // let's find what version of the library we're running
                    val version: String = io.humble.video_native.Version.getVersionInfo()
                    println("Humble Version: $version")
                }
                cmd.hasOption("help") || args.isEmpty() -> {
                    val formatter = HelpFormatter()
                    formatter.printHelp(DecodeAndPlayVideo::class.java.canonicalName + " <filename>", options)
                }
                else -> {
                    val parsedArgs: Array<String> = cmd.args
                    for (arg in parsedArgs) playVideo(arg)
                }
            }
        } catch (e: ParseException) {
            System.err.println("Exception parsing command line: " + e.localizedMessage)
        }
    }
}

