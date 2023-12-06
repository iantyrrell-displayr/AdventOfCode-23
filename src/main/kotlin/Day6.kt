class Day6 : Day {
    override fun partOne(s: List<String>) =
        s[0].split(":").last().toNumList()
            .zip(s[1].split(":").last().toNumList())
            .map { data ->
                (1 until data.first).map { chargeTime ->
                    val remainingTime = data.first - chargeTime
                    chargeTime * remainingTime
                }.filter { it > data.second }.size
            }.reduce { acc, i -> acc * i }.toString()

    private fun String.toNumList() = split(" ")
        .toList()
        .filter { it.isNotBlank() }
        .map { it.trim().toLong() }


    override fun partTwo(s: List<String>) = partOne(s.map { it.replace(" ", "") })
}
