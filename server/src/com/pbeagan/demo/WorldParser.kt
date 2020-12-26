package com.pbeagan.demo

import com.pbeagan.data.Mob
import com.pbeagan.data.RoomData
import com.pbeagan.data.RoomDirectionData
import com.pbeagan.util.startsWith

object WorldParser {
//    val mobs: MutableList<Mob> = mutableListOf()
//    val rooms: MutableList<RoomData> = mutableListOf()
//    fun parse(s: String) {
//        s.split("START_ROOM").forEach { roomString ->
//            val builder = RoomData.Builder(roomString.first().toInt())
//            val terrain = roomString.substringAfterLast("terrain=")
//            roomString.substringBeforeLast("terrain=").split('\n').forEach {
//                when {
//                    it.startsWith("name=") -> builder.name = it.substringAfter("name=")
//                    it.startsWith("exit=") -> builder.exits.add(
//                        RoomDirectionData.from(it)
//                    )
//                }
//            }
//        }
//    }
//
//    val s = """
//        START_ROOM
//        name=Test
//        exit=w 1 flags=1 preview=
//        terrain=
//        ''''
//        ''''
//        ''''
//    """.trimIndent()
}