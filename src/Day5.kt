import kotlin.math.sign

data class Point(val x: Int, val y: Int) : Comparable<Point> {
  override fun compareTo(other: Point): Int {
    return other.x.compareTo(x) + other.y.compareTo(y)
  }
}

class PointIterator(private val start: Point, private val endInclusive: Point) : Iterator<Point> {
  private val dirX = (endInclusive.x - start.x).sign
  private val dirY = (endInclusive.y - start.y).sign
  private var lastValue: Point? = null

  override fun hasNext(): Boolean {
    return lastValue != endInclusive
  }

  override fun next(): Point {
    return if (lastValue == null) {
        start
      } else {
        Point(x = lastValue!!.x + dirX, y = lastValue!!.y + dirY)
      }
      .also { lastValue = it }
  }
}

data class Line(override val start: Point, override val endInclusive: Point) :
  ClosedRange<Point>, Iterable<Point> {
  override fun iterator(): Iterator<Point> {
    return PointIterator(start, endInclusive)
  }

  val isHorizontal: Boolean
    get() = start.y == endInclusive.y

  val isVertical: Boolean
    get() = start.x == endInclusive.x
}

fun main() = Day5().run()

class Day5 : Runnable {
  private val input by lazy {
    readInput()
      .map { line ->
        line
          .split(" -> ")
          .map { point ->
            val coords = point.trim().split(",").map { it.toInt() }
            check(coords.size == 2)
            Point(coords[0], coords[1])
          }
          .chunked(2)
          .map { (x, y) -> Line(x, y) }
      }
      .flatten()
  }

  override fun run() {
    // Part 1
    input
      .filter { it.isHorizontal || it.isVertical }
      .flatten()
      .fold(mutableMapOf<Point, Int>()) { acc, point ->
        acc.apply { acc[point] = (acc[point] ?: 0) + 1 }
      }
      .let { it.values.count { it > 1 } }
      .also { println("Part I: $it") }

    // Part 2
    input
      .flatten()
      .fold(mutableMapOf<Point, Int>()) { acc, point ->
        acc.apply { acc[point] = (acc[point] ?: 0) + 1 }
      }
      .let { it.values.count { it > 1 } }
      .also { println("Part II: $it") }
  }
}
