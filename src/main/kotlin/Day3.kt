data class Day3Part(val start: Int, val end: Int, val num: Int)
class Day3 : Day {
    private var currentString = ""
    private val parts = mutableMapOf<Int, MutableList<Day3Part>>()
    private val symbols = mutableMapOf<Int, MutableList<Int>>()

    private fun extractPartsAndSymbols(s: List<String>, onlyGears: Boolean = false) {
        parts.clear()
        symbols.clear()

        s.forEachIndexed { i, it ->
            it.forEachIndexed { j, char ->
                if (char.isDigit()) {
                    currentString += char
                } else {
                    addPart(i, j, currentString)

                    if (char != '.' && (char == '*' || !onlyGears)) {
                        addSymbol(i, j)
                    }
                }
            }
            addPart(i, it.length, currentString)
        }
    }

    private fun addSymbol(i: Int, pos: Int) {
        symbols[i] = symbols.getOrDefault(i, mutableListOf()).also { it.add(pos) }
    }

    private fun addPart(i: Int, end: Int, part: String) {
        if (part.isNotBlank()) {
            parts[i] = parts.getOrDefault(i, mutableListOf())
                .also { it.add(Day3Part(end - part.length, end - 1, part.toInt())) }
            currentString = ""
        }
    }

    override fun partOne(s: List<String>): String {
        fun nearSymbol(i: Int, part: Day3Part): Boolean {
            val rows = listOf(i - 1, i, i + 1).filter { it >= symbols.keys.min() && it <= symbols.keys.max() }
            return rows.any { symbols[it]?.any { s -> s >= part.start - 1 && s <= part.end + 1 } ?: false }
        }

        extractPartsAndSymbols(s)

        return parts.entries.flatMap {
            it.value.filter { part ->
                nearSymbol(it.key, part)
            }
        }
            .sumOf { it.num }
            .toString()

    }

    override fun partTwo(s: List<String>): String {
        extractPartsAndSymbols(s, true)

        return symbols.flatMap {
            it.value.map { gear -> nearbyParts(it.key, gear) }
        }.sum().toString()
    }

    private fun nearbyParts(i: Int, gear: Int): Int {
        val rows = listOf(i - 1, i, i + 1).filter { it >= parts.keys.min() && it <= parts.keys.max() }
        val nearbyParts =
            rows.mapNotNull { parts[it]?.filter { p -> gear >= p.start - 1 && gear <= p.end + 1 }?.map { p -> p.num } }
                .flatten()

        return if (nearbyParts.size == 2) nearbyParts.fold(1) { acc, p -> acc * p } else 0
    }
}
