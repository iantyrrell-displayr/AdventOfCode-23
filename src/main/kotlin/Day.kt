interface Day {
    fun partOne(s: List<String>): String
    fun partTwo(s: List<String>): String


    fun String.toLongList() = split(" ")
        .toList()
        .filter { it.isNotBlank() }
        .map { it.trim().toLong() }

    fun String.toIntList() = split(" ")
        .toList()
        .filter { it.isNotBlank() }
        .map { it.trim().toInt() }
}
