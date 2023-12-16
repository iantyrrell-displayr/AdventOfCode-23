class Day15 : Day {
    data class Lens(val name: String, val focalLength: Int)

    override fun partOne(s: List<String>) = s[0]
        .split(",").sumOf { hash(it) }
        .toString()

    private fun hash(it: String) = it.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }.toInt()

    override fun partTwo(s: List<String>): String {
        val lightBoxes = mutableMapOf<Int, MutableList<Lens>>().apply {
            for (i in 0..255) {
                put(i, mutableListOf())
            }
        }

        s[0].split(",").forEach { ss ->
            val sss = ss.split("=", "-")
            val isRemove = ss.endsWith("-")
            val label = sss[0]
            val box = hash(label)

            lightBoxes[box]?.apply {
                if (isRemove) {
                    removeIf { it.name == label }
                } else {
                    if (indexOfFirst { it.name == label } == -1) {
                        add(Lens(label, sss[1].toInt()))
                    } else {
                        replaceAll { if (it.name == label) Lens(label, sss[1].toInt()) else it }
                    }
                }
            }
        }

        return lightBoxes.entries.fold(0) { acc, entry ->
            acc + entry.value.foldIndexed(0) { i, acc2, lens -> acc2 + ((1 + entry.key) * (i + 1) * lens.focalLength) }
        }.toString()
    }
}
