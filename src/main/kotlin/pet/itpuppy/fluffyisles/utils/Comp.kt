package pet.itpuppy.fluffyisles.utils

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent

object Comp {
    fun of(text: String) = Component.literal(text)
    fun of(text: String, color: ChatFormatting) = Component.literal(text).withStyle(color)

    fun build(vararg components: Component): MutableComponent {
        val result = Component.empty()
        components.forEach { result.append(it) }
        return result
    }

    fun MutableComponent.withBold(bold: Boolean): MutableComponent {
        return this.withStyle { s -> s.withBold(bold) }
    }

    fun MutableComponent.withHoverText(text: String): MutableComponent {
        return this.withStyle { s ->
            s.withHoverEvent(HoverEvent.ShowText(of(text)))
        }
    }

    fun MutableComponent.withHoverText(text: Component): MutableComponent {
        return this.withStyle { s ->
            s.withHoverEvent(HoverEvent.ShowText(text))
        }
    }

    fun MutableComponent.suffix(text: Component) = this.copy().append(text)
    fun MutableComponent.suffix(text: String) = this.copy().append(text)

    fun MutableComponent.prefix(text: Component) = text.copy().append(this)
    fun MutableComponent.prefix(text: String) = of(text).append(this)
}