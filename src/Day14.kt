data class Window(val name: String, val count: Long)

fun main() = Day14().run()

class Day14 : Runnable {
  private val input by lazy {
    val lines = readInput()
    val instructions =
      lines.drop(2).map { it.split(" -> ") }.associate { (pair, insert) ->
        pair to listOf(pair.first() + insert, insert + pair.last())
      }
    val polymers = lines.first().windowed(2).map { Window(it, 1) }
    (polymers to instructions)
  }

  private fun solve(steps: Int) {
    val (polymer, instructions) = input
    val firstPolymer = polymer.first()

    (1..steps)
      .fold(polymer) { windows, _ ->
        windows
          .flatMap { (name, count) -> instructions[name]!!.map { Window(it, count) } }
          .groupingBy { it.name }
          .reduce { _, accumulator, element ->
            accumulator.copy(count = accumulator.count + element.count)
          }
          .values
          .toList()
      }
      .flatMap { (name, count) -> listOf(name.last() to count) }
      .fold(mutableMapOf<Char, Long>()) { acc, (char, count) ->
        acc.apply { this[char] = (this[char] ?: 0) + count }
      }
      .mapValues { (char, count) -> if (char == firstPolymer.name.first()) count + 1 else count }
      .let {
        val leastCommon = it.entries.minOf { it.value }
        val mostCommon = it.entries.maxOf { it.value }
        println(mostCommon - leastCommon)
      }
  }

  override fun run() {
    // Part I
    solve(steps = 10)

    // Part II
    solve(steps = 40)
  }
}
