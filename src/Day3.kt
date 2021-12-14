fun main() = Day3().run()

class Day3 : Runnable {
  private val input by lazy {
    val bundle = readInput()
    bundle
  }

  override fun run() {
    partOne()
    partTwo()
  }

  private fun partOne() {
    // Part 1
    val mostCommonBits =
      input
        .map { it.toCharArray().map { char -> char.digitToInt() } }
        .reduce { acc, row -> acc.mapIndexed { index: Int, value: Int -> row[index] + value } }
        .map { if (it > input.size - it) 0 else 1 }
    val leastCommonBits = mostCommonBits.map { if (it == 0) 1 else 0 }

    val gamma = mostCommonBits.toDecimal()
    val epsilon = leastCommonBits.toDecimal()
    val power = gamma * epsilon
    println("gamma=$gamma, epsilon=$epsilon, power=$power")
  }

  private fun partTwo() {
    // Part 2
    var oxygen = input.map { it.toCharArray().map { char -> char.digitToInt() } }
    for (position in input[0].indices) {
      if (oxygen.size == 1) break
      val bit1Count = oxygen.count { it[position] == 1 }
      oxygen = oxygen.filter { it[position] == if (bit1Count >= oxygen.size - bit1Count) 1 else 0 }
    }

    var co2 = input.map { it.toCharArray().map { char -> char.digitToInt() } }
    for (position in input[0].indices) {
      if (co2.size == 1) break
      val bit1Count = co2.count { it[position] == 1 }
      co2 = co2.filter { it[position] == if (bit1Count < co2.size - bit1Count) 1 else 0 }
    }

    val o2 = oxygen.flatten().toDecimal()
    val scrubber = co2.flatten().toDecimal()
    val support = o2 * scrubber
    println("o2=$o2, co2=$scrubber, life support=$support")
  }
}

private fun List<Int>.toDecimal(): Int {
  return joinToString(separator = "") { it.toString() }.toInt(radix = 2)
}
