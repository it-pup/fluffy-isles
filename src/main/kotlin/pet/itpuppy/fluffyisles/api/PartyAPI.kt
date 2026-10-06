package pet.itpuppy.fluffyisles.api

import pet.itpuppy.fluffyisles.api.ConnectionAPI.onIsles
import pet.itpuppy.fluffyisles.collectors.ScoreboardCollector

object PartyAPI {
    data class PartyMember(
        val displayName: String,
        val health: Int?
    )

    var inParty: Boolean? = null; private set
    var partyMembers: List<PartyMember>? = null; private set

    private val memberStringRegex = Regex("""P » (?:\[unknown player head\])?((?:\p{Co} )?\w+)(?: ❤(\d+))?§""")

    private fun updateInParty() {
        if (!onIsles) { inParty = null; return }

        inParty = ScoreboardCollector.getOrNull(4)
            ?.trim()
            ?.startsWith("P »")
    }

    private fun updatePartyMembers() {
        if (!onIsles) { partyMembers = null; return }
        if (inParty != true) { partyMembers = null; return }

        val cb = ScoreboardCollector.getFull()
        partyMembers = cb.drop(4).map {
            val match = memberStringRegex.find(it.trim())
            PartyMember(
                match?.groupValues[1] ?: "?",
                match?.groupValues[2]?.toIntOrNull()
            )
        }
    }

    fun tick() {
        updateInParty()
        updatePartyMembers()
    }
}