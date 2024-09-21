package me.odinmain.features.impl.floor7

import me.odinmain.OdinMain.mc
import me.odinmain.features.impl.floor7.WitherDragons.colors
import me.odinmain.features.impl.floor7.WitherDragons.relicAnnounceTime
import me.odinmain.features.impl.floor7.WitherDragons.selected
import me.odinmain.utils.equalsOneOf
import me.odinmain.utils.skyblock.*
import net.minecraft.init.Blocks
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement

object Relic {
    val currentRelic get() = mc.thePlayer?.heldItem?.itemID ?: ""

    enum class Relic (
        val id: String,
        val colorCode: Char
    ) {
        Green("GREEN_KING_RELIC", 'a'),
        Purple("PURPLE_KING_RELIC", '5'),
        Blue("BLUE_KING_RELIC", 'b'),
        Orange("ORANGE_KING_RELIC", '6'),
        Red("RED_KING_RELIC", 'c')
    }

    private val relicPBs = PersonalBest("Relics", 5)
    private var timer = 0L
    var ticks = 0

    fun relicsOnMessage(){
        if (WitherDragons.relicAnnounce) partyMessage("${colors[selected]} Relic")
        timer = System.currentTimeMillis()
        ticks = WitherDragons.relicSpawnTicks
    }

    fun relicsBlockPlace(packet: C08PacketPlayerBlockPlacement) {
        if (timer == 0L || !getBlockAt(packet.position).equalsOneOf(Blocks.cauldron, Blocks.anvil)) return

        Relic.entries.find { it.id == currentRelic }?.let {
            relicPBs.time(it.ordinal, (System.currentTimeMillis() - timer) / 1000.0, "s§7!", "§${it.colorCode}${it.name} relic §7took §6", addPBString = true, addOldPBString = true, sendOnlyPB = false, sendMessage = relicAnnounceTime)
            timer = 0L
        }
    }

    fun onServerTick() {
        ticks--
    }
}