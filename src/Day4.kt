private const val boardSize: Int = 5
private val colIndices =
  (0 until boardSize)
    .map { it until boardSize * boardSize step boardSize }
    .map { it.toList() }
    .flatten()

fun main() = Day4().run()

private class Board(
  private val board: List<String?>,
  private val drawn: List<String> = emptyList(),
) {

  fun play(num: String): Board {
    return Board(board.map { if (it == num) null else it }, drawn + num)
  }

  val isWinner by lazy { isRowWinner() || isColWinner() }

  private fun isRowWinner(): Boolean {
    return board.chunked(boardSize).any { row -> row.all { it == null } }
  }

  private fun isColWinner(): Boolean {
    return board.slice(colIndices).chunked(boardSize).any { col -> col.all { it == null } }
  }

  val score by lazy {
    check(isWinner).let { board.filterNotNull().sumOf { it.toInt() } * drawn.last().toInt() }
  }
}

class Day4 : Runnable {
  private val input by lazy {
    val bundle = readInput()
    bundle
  }

  private val drawn by lazy { input.first().split(",") }

  private val boards by lazy {
    input
      .drop(2)
      .windowed(boardSize, step = boardSize + 1)
      .map { board -> board.flatMap { it.split(Regex("\\s+")) }.filterNot { it.isBlank() } }
      .map { Board(it) }
  }

  override fun run() {
    val finishedBoards =
      drawn
        .fold(boards.map { it to 0 }) { boards, num ->
          val winners = boards.map { it.first }.count { it.isWinner }
          boards.map { (board, place) ->
            if (board.isWinner) {
              board to place
            } else {
              board.play(num).let { if (it.isWinner) it to winners + 1 else it to place }
            }
          }
        }
        .sortedBy { it.second }
        .map { it.first }

    // Part 1
    println("Winner Score: ${finishedBoards.first().score}")

    // Part 2
    println("Looser Score: ${finishedBoards.last().score}")
  }
}
