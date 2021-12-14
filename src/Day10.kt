private val errorScores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
private val autocompleteScores = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
private val braces = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

fun main() = Day10().run()

private val List<Char>.errorScore: Int
  get() {
    fold(ArrayDeque<Char>(size / 2)) { acc, char ->
      acc.apply {
        if (braces.containsKey(char)) {
          acc.add(char)
        } else {
          val popped = acc.removeLast()
          if (braces[popped]!! != char) {
            return errorScores[char]!!
          }
        }
      }
    }
    return 0
  }

private val List<Char>.autocompleteScore: Long
  get() {
    val openBraces =
      fold(ArrayDeque<Char>(size / 2)) { acc, char ->
        acc.apply {
          if (braces.containsKey(char)) {
            acc.add(char)
          } else {
            check(braces[acc.removeLast()] == char)
          }
        }
      }
    return openBraces.mapNotNull { braces[it] }.foldRight(0L) { char, score ->
      score * 5 + autocompleteScores[char]!!
    }
  }

class Day10 : Runnable {
  private val input by lazy {
    readInput().map { it.toCharArray().toList() }
  }

  override fun run() {
    // Part I
    input.sumOf { it.errorScore }.also { println("Error Score: $it") }

    // Part II
    input.filter { it.errorScore == 0 }.map { it.autocompleteScore }.sorted().also {
      println("Autocomplete Score: ${it[it.size / 2]}")
    }
  }
}
