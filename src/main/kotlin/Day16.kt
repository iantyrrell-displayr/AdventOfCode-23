import java.util.Stack

class Day16 : Day {
    private enum class LocationType(val type: Char) {
        EMPTY_SPACE('.'), MIRROR_FORWARD_SLASH('/'), MIRROR_BACK_SLASH('\\'), SPLITTER_VERTICAL('|'), SPLITTER_HORIZONTAL('-');

        fun newDirection(dir: Direction): List<Direction> =
            when (this) {
                EMPTY_SPACE -> listOf(dir)
                MIRROR_FORWARD_SLASH -> {
                    listOf(
                        when (dir) {
                            Direction.UP -> Direction.RIGHT
                            Direction.DOWN -> Direction.LEFT
                            Direction.LEFT -> Direction.DOWN
                            Direction.RIGHT -> Direction.UP
                        }
                    )
                }

                MIRROR_BACK_SLASH -> {
                    listOf(
                        when (dir) {
                            Direction.UP -> Direction.LEFT
                            Direction.DOWN -> Direction.RIGHT
                            Direction.LEFT -> Direction.UP
                            Direction.RIGHT -> Direction.DOWN
                        }
                    )
                }

                SPLITTER_VERTICAL -> {
                    when (dir) {
                        Direction.UP -> listOf(Direction.UP)
                        Direction.DOWN -> listOf(Direction.DOWN)
                        Direction.LEFT -> listOf(Direction.UP, Direction.DOWN)
                        Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
                    }
                }

                SPLITTER_HORIZONTAL -> {
                    when (dir) {
                        Direction.UP -> listOf(Direction.LEFT, Direction.RIGHT)
                        Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)
                        Direction.LEFT -> listOf(Direction.LEFT)
                        Direction.RIGHT -> listOf(Direction.RIGHT)
                    }
                }
            }
    }

    private enum class Direction { UP, DOWN, LEFT, RIGHT }
    private data class Location(val type: LocationType, val directionsVisited: MutableSet<Direction> = mutableSetOf())
    private data class Beam(var coordinate: Coordinate, var heading: Direction)

    private fun buildMap(s: List<String>): Map<Coordinate, Location> {
        val map = mutableMapOf<Coordinate, Location>()
        s.forEachIndexed { y, row ->
            row.filterNot { it == '\r' }
                .forEachIndexed { x, c ->
                    map[Coordinate(x, y)] = when (c) {
                        '.' -> Location(LocationType.EMPTY_SPACE)
                        '/' -> Location(LocationType.MIRROR_FORWARD_SLASH)
                        '\\' -> Location(LocationType.MIRROR_BACK_SLASH)
                        '|' -> Location(LocationType.SPLITTER_VERTICAL)
                        '-' -> Location(LocationType.SPLITTER_HORIZONTAL)
                        else -> throw Exception("Unknown location type: $c")
                    }
                }
        }
        return map
    }

    private fun getEnergizedTileCount(s: List<String>, initialBeam: Beam): Int {
        val tileMap = buildMap(s)
        val isValid: (coord: Coordinate, heading: Direction) -> Boolean = { coord, heading -> tileMap.containsKey(coord) && !tileMap[coord]!!.directionsVisited.contains(heading) }
        val beams = Stack<Beam>().apply { push(initialBeam) }

        while (beams.isNotEmpty()) {
            val beam = beams.pop()
            val location = tileMap[beam.coordinate]!!.apply { directionsVisited.add(beam.heading) }

            beams.addAll(location.type.newDirection(beam.heading).mapNotNull {
                val newCoordinate = when (it) {
                    Direction.UP -> Coordinate(beam.coordinate.first, beam.coordinate.second - 1)
                    Direction.DOWN -> Coordinate(beam.coordinate.first, beam.coordinate.second + 1)
                    Direction.LEFT -> Coordinate(beam.coordinate.first - 1, beam.coordinate.second)
                    Direction.RIGHT -> Coordinate(beam.coordinate.first + 1, beam.coordinate.second)
                }

                if (!isValid(newCoordinate, it)) null else Beam(newCoordinate, it)
            })
        }
        return tileMap.count { it.value.directionsVisited.size > 0 }
    }

    override fun partOne(s: List<String>) = getEnergizedTileCount(s, Beam(Coordinate(0, 0), Direction.RIGHT)).toString()
    override fun partTwo(s: List<String>): String {
        val tileMap = buildMap(s)
        val maxTile = tileMap.entries.last().key
        val initialBeams =
            (0..maxTile.first).map { Beam(Coordinate(it, 0), Direction.DOWN) } +
            (0..maxTile.first).map { Beam(Coordinate(it, maxTile.second), Direction.UP) } +
            (0..maxTile.second).map { Beam(Coordinate(0, it), Direction.RIGHT) } +
            (0..maxTile.second).map { Beam(Coordinate(maxTile.first, it), Direction.LEFT) }

        return initialBeams.maxOf { getEnergizedTileCount(s, it) }.toString()
    }
}
