import java.util.*

fun main() {
    val day = Day2() as Day
    val res = day::class.java.getResource("input-${day.javaClass.name.lowercase(Locale.getDefault())}.txt")
//    val res = day::class.java.getResource("input-${day.javaClass.name.lowercase(Locale.getDefault())}-sample.txt")
    val input = res?.readText()?.split("\n") ?: emptyList()

    println(day.partOne(input))
    println()
    println(day.partTwo(input))
}