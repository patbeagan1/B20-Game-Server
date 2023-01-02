package com.pbeagan.actions

import com.pbeagan.data.Mob
import com.pbeagan.demo.SampleData
import rooms
import java.util.Locale

class Debug(private val target: String) : Action(), FreeAction {

    override fun invoke(self: Mob) {
        searchMobs(self) ?: searchMobItems(self) ?: searchRoomItems(self)
    }

    private fun searchRoomItems(self: Mob): Unit? = rooms.map { it.value.items }.flatten().firstOrNull {
        it.nameStartsWith(target)
    }?.let {
        writer.sayTo(self).info(it.toString())
    }

    private fun searchMobItems(self: Mob): Unit? = SampleData.mobs.map { it.items }.flatten().firstOrNull {
        it.nameStartsWith(target)
    }?.let {
        writer.sayTo(self).info(it.toString())
    }

    private fun searchMobs(self: Mob): Unit? = SampleData.mobs.firstOrNull {
        it.nameBase.lowercase(Locale.getDefault()) == target.lowercase(Locale.getDefault())
    }?.let { currentMob ->
        writer.sayTo(self).run {
            info(currentMob.toString())
            info(currentMob.effects.toString())
            info("")
            info("baseAtkMelee: ${currentMob.baseAtkMelee}")
            info("baseAtkRanged: ${currentMob.baseAtkRanged}")
            info("baseAtkThrow: ${currentMob.baseAtkThrow}")
            info("awareness: ${currentMob.awareness}")
            info("spirit: ${currentMob.spirit}")
            info("speed: ${currentMob.speed}")
            info("presence: ${currentMob.presence}")
            info("cunning: ${currentMob.cunning}")
            info("persuasion: ${currentMob.persuasion}")
            info("tenacity: ${currentMob.tenacity}")
            info("fortitude: ${currentMob.fortitude}")
            info("strength: ${currentMob.strength}")
            info("agility: ${currentMob.agility}")
            info("precision: ${currentMob.precision}")
            info("endurance: ${currentMob.endurance}")
            info("durability: ${currentMob.durability}")
            info("totalHearts: ${currentMob.totalHearts}")
            info("description: ${currentMob.description}")
            info("preferredAttack: ${currentMob.preferredAttack}")
            info("idForIO: ${currentMob.idForIO}")
            info("hearts: ${currentMob.hearts}")
            info("")
        }
    }
}