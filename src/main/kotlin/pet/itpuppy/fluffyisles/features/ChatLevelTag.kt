package pet.itpuppy.fluffyisles.features

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import pet.itpuppy.fluffyisles.FluffyIsles
import pet.itpuppy.fluffyisles.api.ChatMessageAPI
import pet.itpuppy.fluffyisles.utils.Comp
import pet.itpuppy.fluffyisles.utils.FormatCodes

object ChatLevelTag {
    private val enabled get() = FluffyIsles.config.chatSection.levelTagEnabled

    fun modify(message: Component): Component {
        if (!enabled) return message

        val sender = ChatMessageAPI.parse(message)?.sender ?: return message
        val level = ChatMessageAPI.totalLevelOf(message) ?: return message

        return insert(message, sender, level) ?: message
    }

    private fun insert(component: Component, sender: String, level: ChatMessageAPI.TotalLevel): MutableComponent? {
        val copy = component.copy()
        val index = copy.siblings.indexOfFirst { it.string == sender }
        if (index == -1) return null

        copy.siblings.add(index, tag(copy.style, level))
        return copy
    }

    private fun tag(style: Style, level: ChatMessageAPI.TotalLevel): MutableComponent {
        val hover = style.hoverEvent
        val click = style.clickEvent
        val bracket = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withHoverEvent(hover).withClickEvent(click)
        val number = level.style.withHoverEvent(hover).withClickEvent(click)

        return Comp.build(
            Comp.of("[").withStyle(bracket),
            Comp.of(level.value.toString()).withStyle(number),
            Comp.of("] ").withStyle(bracket),
        )
    }
}