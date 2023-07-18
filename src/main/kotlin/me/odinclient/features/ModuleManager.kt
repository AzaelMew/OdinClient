package me.odinclient.features

import me.odinclient.utils.ClassUtils
import me.odinclient.utils.ClassUtils.instance
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.lang.reflect.Modifier

object ModuleManager {

    val modules: ArrayList<Module> = arrayListOf<Module>().apply {
        ClassUtils.findClasses<Module>("me.odinclient.features.impl")
            .filter { Modifier.isFinal(it.modifiers) }
            .forEach { add(it.instance) }
    }

    @SubscribeEvent
    fun activateModuleKeyBinds(event: InputEvent.KeyInputEvent) {
        if (Keyboard.getEventKeyState()) return
        val eventKey = Keyboard.getEventKey()
        if (eventKey == 0) return
        modules.filter { it.keyCode == eventKey }.forEach { it.keyBind() }
    }

    @SubscribeEvent
    fun activateModuleMouseBinds(event: InputEvent.MouseInputEvent) {
        if (Mouse.getEventButtonState()) return
        val eventButton = Mouse.getEventButton()
        if (eventButton == 0) return
        modules.filter { it.keyCode + 100 == eventButton }.forEach { it.keyBind() }
    }

    fun getModuleByName(name: String): Module? = modules.firstOrNull { it.name.equals(name, true) }
}