sealed class Cave(open val name: String) {
    companion object {
      fun fromString(s: String) =
        when {
          s == "start" -> Start
          s == "end" -> End
          s.toCharArray().all { it.isLowerCase() } -> Small(s)
          s.toCharArray().all { it.isUpperCase() } -> Large(s)
          else -> throw IllegalArgumentException("Unknown cave kind $s")
        }
    }
  }
  
  object Start : Cave(name = "start")
  
  data class Small(override val name: String) : Cave(name)
  
  data class Large(override val name: String) : Cave(name)
  
  object End : Cave(name = "end")
  
  fun main() = Day12().run()
  
  fun Map<Cave, List<Cave>>.visit(
    smallCaveSingleVisit: Boolean,
    node: Cave = Start,
    path: List<Cave> = listOf(Start),
  ): List<List<Cave>> {
    if (node == End) return listOf(path)
    val links = this[node]!!
    val suitableLinks =
      if (smallCaveSingleVisit) {
        links.filterNot { it is Start }.filterNot { it is Small && path.contains(it) }
      } else {
        val smallPathCounts = path.filterIsInstance<Small>().groupingBy { it }.eachCount()
        links.filterNot { it is Start }.filterNot {
          it is Small && smallPathCounts.any { it.value == 2 } && path.contains(it)
        }
      }
  
    return suitableLinks.flatMap { visit(smallCaveSingleVisit, it, path + it) }
  }
  
  class Day12 : Runnable {
    private val input by lazy {
      readInput()
        .map { it.split("-").map(Cave.Companion::fromString) }
        .map { (a, b) -> listOf(a to b, b to a) }
        .flatten()
        .groupBy { it.first }
        .mapValues { (_, value) -> value.map { (_, b) -> b } }
    }
  
    override fun run() {
      // Part I
      println(input.visit(smallCaveSingleVisit = true).size)
  
      // Part I
      println(input.visit(smallCaveSingleVisit = false).size)
    }
  }
  