fun main() = Day6().run()

class Day6 : Runnable {
  private val input by lazy {
    readInput().first().split(",").map { it.toInt() }.groupingBy { it }.eachCount().mapValues {
      (_, value) ->
      value.toLong()
    }
  }

  private fun Map<Int, Long>.tick(): Map<Int, Long> {
    return mapKeys { (key, _) -> key - 1 }.toMutableMap().apply {
      this[8] = this[-1] ?: 0
      this[6] = (this[6] ?: 0) + (this[-1] ?: 0)
      remove(-1)
    }
  }

  override fun run() {
    // Part I
    (1..80).fold(input) { fish, _ -> fish.tick() }.values.sum().also { println(it) }

    // Part II
    (1..256).fold(input) { fish, _ -> fish.tick() }.values.sum().also { println(it) }
  }
}
