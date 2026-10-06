package pet.itpuppy.fluffyisles.api

import pet.itpuppy.fluffyisles.FluffyIsles
import pet.itpuppy.fluffyisles.collectors.ScoreboardCollector

object ConnectionAPI {
    var onIsles: Boolean = false; private set
    var lobby: String? = null; private set
    var isHub: Boolean? = null; private set
    var version: String? = null; private set

    private fun updateOnIsles() {
        onIsles = FluffyIsles.client.connection?.serverData?.ip in setOf("play.skyblockisles.net", "skyblockisles.net")
    }

    private fun updateLobby() {
        if (!onIsles) { lobby = null; return }
        lobby = ScoreboardCollector.getOrNull(2)?.substringBefore(" ")
    }

    private fun updateIsHub() {
        if (!onIsles) { isHub = null; return }
        isHub = lobby?.startsWith("Hub")
    }

    private fun updateVersion() {
        if (!onIsles) { version = null; return }
        version = ScoreboardCollector.getOrNull(2)
            ?.substringAfter(" ")
            ?.trim()
            ?.removeSuffix("§\u0081")
    }

    fun tick() {
        updateOnIsles()
        updateLobby()
        updateIsHub()
        updateVersion()
    }
}