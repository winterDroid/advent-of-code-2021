fun main() = Day1().run()

class Day1 : Runnable {
  private val input by lazy {
    readInput().map { it.toInt() }
  }

  override fun run() {
    // Part One
    println(input.windowed(2).count { it[0] < it[1] })

    // Part Two
    println(input.windowed(3) { it.sum() }.windowed(2).count { it[0] < it[1] })
  }
}
