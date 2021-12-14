fun main() = Day2().run()

data class Position(val x: Int, val y: Int, val aim: Int)

sealed class Direction

data class Up(val y: Int) : Direction()

data class Down(val y: Int) : Direction()

data class Forward(val x: Int) : Direction()

private fun List<String>.toDirection(): Direction {
  val (name, value) = this
  return when (name) {
    "up" -> Up(value.toInt())
    "down" -> Down(value.toInt())
    "forward" -> Forward(value.toInt())
    else -> throw IllegalArgumentException()
  }
}

class Day2 : Runnable {
  private val input by lazy {
    readInput().map { it.split(" ") }.map { it.toDirection() }
  }

  override fun run() {
    // Part 1
    input
      .fold(Position(0, 0, 0)) { position, direction: Direction ->
        when (direction) {
          is Down -> position.copy(y = position.y + direction.y)
          is Forward -> position.copy(x = position.x + direction.x)
          is Up -> position.copy(y = position.y - direction.y)
        }
      }
      .also { (x, y) -> println("x=$x, y=$y ==> ${x * y}") }

    // Part 2
    input
      .fold(Position(0, 0, 0)) { position, direction: Direction ->
        when (direction) {
          is Down -> position.copy(aim = position.aim + direction.y)
          is Forward ->
            position.copy(
              x = position.x + direction.x,
              y = position.y + (position.aim * direction.x)
            )
          is Up -> position.copy(aim = position.aim - direction.y)
        }
      }
      .also { (x, y, aim) -> println("x=$x, y=$y, aim=$aim ==> ${x * y}") }
  }
}
