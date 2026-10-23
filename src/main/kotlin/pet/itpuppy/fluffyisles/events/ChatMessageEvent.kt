package pet.itpuppy.fluffyisles.events

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.Component
import pet.itpuppy.fluffyisles.features.ChatLevelTag
import pet.itpuppy.fluffyisles.features.GlobalChatMute
import pet.itpuppy.fluffyisles.features.PlayerMute
import pet.itpuppy.fluffyisles.features.WhiteChat

object ChatMessageEvent : ClientEvent {
    override fun register() {
        ClientReceiveMessageEvents.ALLOW_GAME.register { message, overlay ->
            overlay || allow(message)
        }

        ClientReceiveMessageEvents.ALLOW_CHAT.register { message, _, _, _, _ ->
            allow(message)
        }

        ClientReceiveMessageEvents.MODIFY_GAME.register { message, overlay ->
            if (overlay) message else WhiteChat.modify(ChatLevelTag.modify(message))
        }
    }
    
    private fun allow(message: Component): Boolean {
        return !GlobalChatMute.shouldBlock(message) && PlayerMute.handle(message)
    }
}