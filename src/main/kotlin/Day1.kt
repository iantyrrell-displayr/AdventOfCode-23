class Day1: Day {
    override fun partOne(s: List<String>): String {

        return s
            .map { it.filter { c -> c.isDigit() } }
            .filter { it.isNotEmpty() }
            .map { "${it.first()}${it.last()}" }
            .sumOf { it.toInt() }
            .toString()
    }

    override fun partTwo(s: List<String>): String {

        val words = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
            )

        /*
        We run into issues with, e.g. eightwothree if we replace the numbers in order
            - eightwothree
            - eigh23
            - 23 -- WRONG
        We could walk through the list, but a simpler option is to just surround the replacement number with the first and last letters of the word
            - eightwothree
            - e8t2ot3e
            - 823 -- CORRECT

         */

        return s
            .map { words.entries.fold(it) { acc, entry -> acc.replace(entry.key, "${entry.key.first()}${entry.value}${entry.key.last()}") } }
//            .let { print(it.joinToString("\n") + "\n\n"); it }
            .map { it.filter { c -> c.isDigit() } }
//            .let { print(it.joinToString("\n") + "\n\n"); it }
            .filter { it.isNotEmpty() }
            .map { "${it.first()}${it.last()}" }
//            .let { print(it.joinToString("\n") + "\n\n"); it }
            .sumOf { it.toInt() }
            .toString()
    }

}