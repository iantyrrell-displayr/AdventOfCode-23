import kotlin.math.pow

class Day4 : Day {
    override fun partOne(s: List<String>) =
        s.map { it.split(":").last().split("|").map { s -> s.trim().split(" ").filter { ss -> ss.isNotBlank() } } }
            .map { it.first().intersect(it.last().toSet()) }
            .filter { it.isNotEmpty() }
            .sumOf { 2.0.pow((it.size - 1).toDouble()) }
            .toString()

    override fun partTwo(s: List<String>): String {
        val cards =
            s.map { it.split(":").last().split("|").map { s -> s.trim().split(" ").filter { ss -> ss.isNotBlank() } } }
                .mapIndexed { i, it -> i + 1 to it.first().intersect(it.last().toSet()).size }
                .toMap()

        val newCards = mutableMapOf<Int, Int>()

        cards.keys.reversed().forEach { k ->
            val wins = cards[k] ?: 0
            newCards[k] = wins +
                    ((k + 1 until k + wins + 1)
                        .sumOf { newCards[it] ?: 0 })
        }

        return (newCards.values.sum() + cards.size).toString()
    }
}
