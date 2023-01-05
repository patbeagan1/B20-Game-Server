package com.pbeagan.demo

object WorldParser {
//    val mobs: MutableList<Mob> = mutableListOf()
//    val rooms: MutableList<RoomData> = mutableListOf()
//    fun parse(s: String) {
//        s.split("START_ROOM").forEach { roomString ->
//            val builder = RoomData.Builder(roomString.first().toInt())
//            val terrain = roomString.substringAfterLast("terrain=")
//            roomString.substringBeforeLast("terrain=").split('\n').forEach {
//                when {
//                    it.startsWithIgnoreCase("name=") -> builder.name = it.substringAfter("name=")
//                    it.startsWithIgnoreCase("exit=") -> builder.exits.add(
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