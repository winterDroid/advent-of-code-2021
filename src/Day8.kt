//  aaaa
// b    c
// b    c
//  dddd
// e    f
// e    f
//  gggg
data class Signal(val intValue: Int, val chars: Set<Char>)

private val allMappings by lazy {
  fun permutate(list: List<Char>): Set<List<Char>> =
    if (list.isEmpty()) {
      setOf(emptyList())
    } else {
      buildSet {
        list.indices.forEach { i ->
          addAll(permutate(list - list[i]).map { item -> item + list[i] })
        }
      }
    }

  val chars = 'a'..'g'
  permutate(chars.toList()).map { chars.zip(it).toMap() }
}

private val letters =
  listOf(
    setOf('a', 'b', 'c', 'e', 'f', 'g'),
    setOf('c', 'f'),
    setOf('a', 'c', 'd', 'e', 'g'),
    setOf('a', 'c', 'd', 'f', 'g'),
    setOf('b', 'c', 'd', 'f'),
    setOf('a', 'b', 'd', 'f', 'g'),
    setOf('a', 'b', 'd', 'e', 'f', 'g'),
    setOf('a', 'c', 'f'),
    setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
    setOf('a', 'b', 'c', 'd', 'f', 'g')
  )
    .mapIndexed { index, chars -> Signal(index, chars) }
    .groupingBy { it.chars }
    .reduce { _, _, element -> element }

data class Segment(val signalPattern: List<String>, val output: List<String>) {
  private val mapping by lazy {
    allMappings.first { perm ->
      signalPattern.all { pattern ->
        val mapped = pattern.map { perm[it] }.toSet()
        letters.containsKey(mapped)
      }
    }
  }

  val decodedValue: Int by lazy {
    val mapping = mapping
    output
      .map { it.map { mapping[it]!! }.toSet() }
      .map { letters[it]!!.intValue }
      .joinToString("") { it.toString() }
      .toInt()
  }
}

fun main() = Day8().run()

class Day8 : Runnable {
  private val input by lazy {
    readInput().map { line ->
      val (signalPattern, output) = line.split("|").map { it.trim() }.map { it.split(" ") }
      Segment(signalPattern, output)
    }
  }

  override fun run() {
    // Part I
    println(input.flatMap { it.output }.count { it.length in listOf(2, 3, 4, 7) })

    // Part II
    println(input.sumOf { it.decodedValue })
  }
}
