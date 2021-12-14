data class Coordinate(val row: Int, val col: Int) {
    val adjacentCoordinates by lazy {
      listOf(
        Coordinate(row + 1, col),
        Coordinate(row - 1, col),
        Coordinate(row, col + 1),
        Coordinate(row, col - 1)
      )
    }
  
    override fun toString(): String {
      return "$row:$col"
    }
  }
  
  data class Basin(val coords: Set<Coordinate>) {
    val size: Int
      get() = coords.size
  }
  
  private fun Map<Coordinate, Int>.isLowestPoint(coord: Coordinate): Boolean {
    return (coord.adjacentCoordinates).all { (this[it] ?: 10) > this[coord]!! }
  }
  
  private fun Map<Coordinate, Int>.containingBasin(lowPoint: Coordinate): Basin {
    check(isLowestPoint(lowPoint))
    val heightMap = this
    val basin = buildSet {
      fun visitAdjacentCoordinates(coord: Coordinate) {
        if (contains(coord)) return
        add(coord)
        coord
          .adjacentCoordinates
          .filter { (heightMap[it] ?: 9) < 9 }
          .filter { heightMap[it]!! > heightMap[coord]!! }
          .forEach { visitAdjacentCoordinates(it) }
      }
      visitAdjacentCoordinates(lowPoint)
    }
    return Basin(basin)
  }
  
  fun main() = Day9().run()
  
  class Day9 : Runnable {
    private val input by lazy {
      readInput()
        .flatMapIndexed { row, line ->
          line.mapIndexed { col, value -> Coordinate(row, col) to value.digitToIntOrNull()!! }
        }
        .toMap()
    }
  
    override fun run() {
      // Part I
      input.filterKeys { coord -> input.isLowestPoint(coord) }.values.sumOf { it + 1 }.also {
        println(it)
      }
  
      // Part II
      input
        .filterKeys { coord -> input.isLowestPoint(coord) }
        .map { (coord, _) -> input.containingBasin(coord) }
        .distinct()
        .sortedByDescending { it.size }
        .take(3)
        .fold(1) { acc, basin -> acc * basin.size }
        .also { println(it) }
    }
  }
  