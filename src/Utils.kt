import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun Any.readInput(): List<String> = File("src", "${javaClass.name}.txt").readLines()
