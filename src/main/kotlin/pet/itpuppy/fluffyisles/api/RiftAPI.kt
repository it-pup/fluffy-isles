package pet.itpuppy.fluffyisles.api

import pet.itpuppy.fluffyisles.api.ConnectionAPI.onIsles
import pet.itpuppy.fluffyisles.collectors.ScoreboardCollector

object RiftAPI {
    var inRift: Boolean? = null; private set

    var riftName: String? = null; private set
    var riftTime: Int? = null; private set

    var riftScore: Int? = null; private set
    var riftScoreMax: Int? = null; private set

    var riftKills: Int? = null; private set
    var riftKillsMax: Int? = null; private set

    var riftSecrets: Int? = null; private set
    var riftSecretsMax: Int? = null; private set

    var riftBosses: Int? = null; private set
    var riftBossesMax: Int? = null; private set

    private val timeLineRegex = Regex("""⌛ Rift Time: (\d+m \d+s) \((\d+)\/(\d+)\)§""")
    private val statsLineRegex = Regex("""☠ (\d+)\/(\d+) 🎁 (\d+)\/(\d+) 👑 (\d+)\/(\d+)§""")
    private val timeRegex = Regex("""(?:(\d+)h)?\s*(?:(\d+)m)?\s*(?:(\d+)s)?""")

    private fun updateInRift() {
        if (!onIsles) { inRift = null; return }
        inRift = ConnectionAPI.lobby?.startsWith("Dungeons")
    }

    private fun updateRiftData() {
        if (!onIsles || inRift != true) {
            riftName = null; riftTime = null
            riftScore = null; riftScoreMax = null
            riftKills = null; riftKillsMax = null
            riftSecrets = null; riftSecretsMax = null
            riftBosses = null; riftBossesMax = null
            return
        }

        val titleLine = ScoreboardCollector.getOrNull(4) ?: "?"
        val timeLine = ScoreboardCollector.getOrNull(5) ?: "?"
        val statsLine = ScoreboardCollector.getOrNull(6) ?: "?"

        riftName = titleLine.trim().removeSuffix(" §\u0083")

        val timeLineMatch = timeLineRegex.find(timeLine.trim())
        riftTime = toSeconds(timeLineMatch?.groupValues[1])
        riftScore = timeLineMatch?.groupValues[2]?.toIntOrNull()
        riftScoreMax = timeLineMatch?.groupValues[3]?.toIntOrNull()

        val statsLineMatch = statsLineRegex.find(statsLine.trim())
        riftKills = statsLineMatch?.groupValues[1]?.toIntOrNull()
        riftKillsMax = statsLineMatch?.groupValues[2]?.toIntOrNull()
        riftSecrets = statsLineMatch?.groupValues[3]?.toIntOrNull()
        riftSecretsMax = statsLineMatch?.groupValues[4]?.toIntOrNull()
        riftBosses = statsLineMatch?.groupValues[5]?.toIntOrNull()
        riftBossesMax = statsLineMatch?.groupValues[6]?.toIntOrNull()
    }

    private fun toSeconds(time: String?): Int? {
        if (time == null) return null
        val match = timeRegex.find(time.trim()) ?: return 0

        val (hours, minutes, seconds) = match.destructured
        return (hours.toIntOrNull() ?: 0) * 3600 +
                (minutes.toIntOrNull() ?: 0) * 60 +
                (seconds.toIntOrNull() ?: 0)
    }

    fun tick() {
        updateInRift()
        updateRiftData()
    }
}