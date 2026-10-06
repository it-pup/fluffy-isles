package pet.itpuppy.fluffyisles.events

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import pet.itpuppy.fluffyisles.api.ConnectionAPI
import pet.itpuppy.fluffyisles.api.PartyAPI
import pet.itpuppy.fluffyisles.api.RiftAPI
import pet.itpuppy.fluffyisles.collectors.ScoreboardCollector

object PerSecondEvent : ClientEvent {
    private var tickCounter = 0
    private const val SECOND = 20

    override fun register() {
        ClientTickEvents.END_CLIENT_TICK.register(::run)
    }

    private fun run(minecraft: Minecraft) {
        if (minecraft.level == null || minecraft.player == null) return

        if (tickCounter++ >= SECOND) {
            collectorCallback()
            apiCallback()

            tickCounter = 0
        }
    }

    // both callbacks need to be ordered manually - some apis depend on other ones
    // so we need to have correct ordering for accurate returns

    private fun collectorCallback() {
        ScoreboardCollector.tick()
    }

    private fun apiCallback() {
        ConnectionAPI.tick()
        PartyAPI.tick()
        RiftAPI.tick()

        println("inRift: ${RiftAPI.inRift}, riftTime: ${RiftAPI.riftTime}, riftKills: ${RiftAPI.riftKills}, riftSecrets: ${RiftAPI.riftSecrets}, riftBosses: ${RiftAPI.riftBosses}, riftScore: ${RiftAPI.riftScore}")
    }
}