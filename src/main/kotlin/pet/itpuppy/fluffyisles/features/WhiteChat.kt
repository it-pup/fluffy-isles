package pet.itpuppy.fluffyisles.features

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import pet.itpuppy.fluffyisles.FluffyIsles
import pet.itpuppy.fluffyisles.api.ChatMessageAPI

object WhiteChat {
    private val MENTION_COLOR = Style.EMPTY.withColor(ChatFormatting.YELLOW).color

    private val enabled get() = FluffyIsles.config.chatSection.whiteChatEnabled

    fun modify(message: Component): Component {
        if (!enabled) return message

        val parsed = ChatMessageAPI.parse(message)
        val separator = if (parsed?.channel == ChatMessageAPI.Channel.DISCORD) ':' else '»'

        if (message.string.trimStart().startsWith(separator)) return message

        return whiten(message, separator) ?: message
    }

    private fun whiten(component: Component, separator: Char): MutableComponent? {
        val segments = component.toFlatList(Style.EMPTY)
        val index = segments.indexOfFirst { it.string.contains(separator) }
        if (index == -1) return null

        val start = if (separator == ':') index else index + 1
        val result = Component.empty()

        for (i in 0 until start) result.append(segments[i])
        for (i in start until segments.size) result.append(segments[i].whiten())

        return result
    }

    private fun Component.whiten(): Component {
        val keep = style.hoverEvent?.action() == HoverEvent.Action.SHOW_ITEM || style.color == MENTION_COLOR
        return if (keep) this else copy().withStyle { it.withColor(ChatFormatting.WHITE) }
    }
}