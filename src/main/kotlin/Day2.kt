enum class Day2Colour { RED, BLUE, GREEN }
data class Day2Game(val cubes: Map<Day2Colour, Int>)
data class Day2Data(val id: Int, val draws: List<Day2Game>)

class Day2 : Day {
    override fun partOne(s: List<String>): String = s
        .map { it.replace("Game ", "") }
        .map {
            val split = it.split(":")
            val cubes = split[1].split(";")
            val games = cubes.map { c ->
                c.split(",").associate { cc ->
                    val sp = cc.trim().split(" ")
                    Day2Colour.valueOf(sp[1].uppercase()) to sp[0].toInt()
                }
            }.map { c -> Day2Game(c) }

            Day2Data(split[0].toInt(), games)
        }
        .filterNot {
            it.draws.any { d2g ->
                d2g.cubes.getOrDefault(Day2Colour.RED, 0) > 12 ||
                        d2g.cubes.getOrDefault(Day2Colour.GREEN, 0) > 13 ||
                        d2g.cubes.getOrDefault(Day2Colour.BLUE, 0) > 14
            }
        }
        .sumOf { it.id }
        .toString()

    override fun partTwo(s: List<String>): String = s
        .map { it.replace("Game ", "") }
        .map {
            val split = it.split(":")
            val cubes = split[1].split(";")
            val maxCubes = cubes.map { c ->
                c.split(",").associate { cc ->
                    val sp = cc.trim().split(" ")
                    Day2Colour.valueOf(sp[1].uppercase()) to sp[0].toInt()
                }
            }.fold(emptyMap<Day2Colour, Int>()) { acc, e ->
                Day2Colour.values().associateWith { v -> acc.getOrDefault(v, 0).coerceAtLeast(e.getOrDefault(v, 0)) }
            }

            Day2Data(split[0].toInt(), listOf(Day2Game(maxCubes)))
        }.sumOf { it.draws[0].cubes.values.fold(1) { acc, i -> acc * i }.toInt() }
        .toString()
}
