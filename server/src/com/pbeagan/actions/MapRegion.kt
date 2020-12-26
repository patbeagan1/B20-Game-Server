//package com.pbeagan.actions
//
//import com.pbeagan.data.Direction
//import com.pbeagan.data.Exits
//import com.pbeagan.data.Mob
//import com.pbeagan.data.RoomDirectionData
//import com.pbeagan.data.currentRoom
//import rooms
//
//class MapRegion : Action(), FreeAction {
//    override fun invoke(self: Mob) {
//        self.currentRoom()?.exits?.asList?.forEach {
//
//        }
//    }
//
//
//    fun findNewRooms(exits: Exits) {
//        val new = getNew(exits.asList)
//        if (new) {
//
//            findNewRooms(it.second.destinationID.let { rooms[it] }
//
//        }
//
//    }
//
//    private val memo = mutableMapOf<Direction, RoomDirectionData>()
//    private fun getNew(asList: List<Pair<Direction, RoomDirectionData>>): List<Pair<Direction, RoomDirectionData>> {
//asList.forEach { if(memo.contains) }    }
//
//}