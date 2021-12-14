import kotlin.math.abs

fun main() = Day7().run()

class Day7 : Runnable {
  private val input by lazy {
    val bundle = readInput().first()
    bundle.split(",").map { it.toInt() }.groupingBy { it }.eachCount()
  }

  override fun run() {
    val min = input.minOf { it.key }
    val max = input.maxOf { it.key }

    // Part I
    (min..max)
      .minOf { alignedPosition ->
        input.entries.sumOf { (pos, number) -> abs(alignedPosition - pos) * number }
      }
      .also { println(it) }

    // Part II
    (min..max)
      .minOf { alignedPosition ->
        input.entries.sumOf { (pos, number) ->
          val n = abs(alignedPosition - pos)
          n * (n + 1) / 2 * number
        }
      }
      .also { println(it) }
  }
}
