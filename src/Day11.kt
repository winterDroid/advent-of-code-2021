fun main() = Day11().run()

data class Coord(val x: Int, val y: Int) {
  val neighbors by lazy {
    listOf(
      Coord(x - 1, y - 1),
      Coord(x - 1, y),
      Coord(x - 1, y + 1),
      Coord(x, y - 1),
      Coord(x, y + 1),
      Coord(x + 1, y - 1),
      Coord(x + 1, y),
      Coord(x + 1, y + 1),
    )
      .filter { it.x in 0..9 && it.y in 0..9 }
  }
}

data class Octopus(val coord: Coord, val level: Int, val flashCounter: Int = 0)

private fun Map<Coord, Octopus>.flash(): Map<Coord, Octopus> {
  return mapValues { it.value.copy(level = it.value.level + 1) }
    .toMutableMap()
    .apply {
      val alreadyFlashed = mutableSetOf<Coord>()
      while (values.any { it.level > 9 }) {
        values.filter { it.level > 9 }.filter { !alreadyFlashed.contains(it.coord) }.forEach {
          this[it.coord] = it.copy(level = 0, flashCounter = it.flashCounter + 1)
          alreadyFlashed.add(it.coord)
          it.coord.neighbors.subtract(alreadyFlashed).forEach {
            this[it] = this[it]!!.copy(level = this[it]!!.level + 1)
          }
        }
      }
    }
    .toMap()
}

class Day11 : Runnable {
  private val input by lazy {
    readInput()
      .mapIndexed { row, s ->
        s.toCharArray().mapIndexed { col, c -> Octopus(Coord(col, row), c.digitToInt()) }
      }
      .flatten()
      .associateBy { it.coord }
  }

  override fun run() {
    // Part I
    (1..100).fold(input) { acc, _ -> acc.flash() }.values.sumOf { it.flashCounter }.also {
      println("FlashCount: $it")
    }

    // Part II
    var currentMap = input
    var step = 0
    while (!currentMap.values.all { it.level == 0 }) {
      step++
      currentMap = currentMap.flash()
    }
    println("Step for all to flash: $step")
  }
}
