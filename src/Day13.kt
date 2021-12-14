data class Dot(val x: Int, val y: Int)

sealed interface Fold

data class Vertical(val x: Int) : Fold

data class Horizontal(val y: Int) : Fold

fun main() = Day13().run()

class Day13 : Runnable {
  private val input by lazy {
    val lines = readInput()
    val separator = lines.indexOf("")
    val board =
      lines
        .subList(0, separator)
        .map { it.split(",") }
        .map { (x, y) -> Dot(x.toInt(), y.toInt()) }
        .toSet()
    val instructions =
      lines.subList(separator + 1, lines.size).map {
        when {
          it.startsWith("fold along x=") -> Vertical(it.drop(13).toInt())
          it.startsWith("fold along y=") -> Horizontal(it.drop(13).toInt())
          else -> throw IllegalArgumentException()
        }
      }
    board to instructions
  }

  override fun run() {
    val (board, instructions) = input
    val folds =
      instructions.scan(board) { board, instruction ->
        when (instruction) {
          is Vertical ->
            board
              .map {
                if (it.x < instruction.x) it else it.copy(x = it.x - (it.x - instruction.x) * 2)
              }
              .toSet()
          is Horizontal ->
            board
              .map {
                if (it.y < instruction.y) it else it.copy(y = it.y - (it.y - instruction.y) * 2)
              }
              .toSet()
        }
      }

    // Part I
    println(folds[1].size)

    // Part II
    println(folds.last().prettyPrint())
  }
}

private fun Set<Dot>.prettyPrint(): String {
  val map = associateBy { "${it.x}:${it.y}" }.mapValues { _ -> "█" }
  val maxX = maxOf { it.x }
  val maxY = maxOf { it.y }

  return (0..maxY).joinToString("\n") { y ->
    (0..maxX).joinToString("") { x -> map.getOrDefault("$x:$y", " ") }
  }
}
