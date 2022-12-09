/*
 * Copyright (c) 2022 Leon Linhart
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package days

import utils.*
import kotlin.math.*

fun main() {
    data class Move(val dir: Direction, val steps: Int)

    val moves = readInput().map { line ->
        val (d, a) = line.split(' ')

        Move(
            dir = when (d) {
                "U" -> Direction.UP
                "D" -> Direction.DOWN
                "L" -> Direction.LEFT
                "R" -> Direction.RIGHT
                else -> error("Unknown direction: $d")
            },
            steps = a.toInt()
        )
    }

    fun Point.isAdjacent(other: Point): Boolean =
        x - other.x in -1..1 && y - other.y in -1..1

    fun solve(length: Int): Int {
        val visited = mutableSetOf<Point>()
        val rope = MutableList(length) { Point(0, 0) }
        visited += rope.last()

        for (move in moves) {
            for (s in 0 until move.steps) {
                rope[0] = move.dir.advance(rope[0])

                for (i in 1 until rope.size) {
                    val prevPos = rope[i - 1]
                    val tailPos = rope[i]

                    if (!tailPos.isAdjacent(prevPos)) {
                        rope[i] = Point(
                            tailPos.x + (prevPos.x - tailPos.x).sign,
                            tailPos.y + (prevPos.y - tailPos.y).sign
                        )
                    }

                }

                visited += rope.last()
            }
        }

        return visited.size
    }

    println("Part 1: ${solve(2)}")
    println("Part 2: ${solve(10)}")
}

private data class Point(val x: Int, val y: Int)

private enum class Direction(val advance: (Point) -> Point) {
    UP({ it.copy(y = it.y - 1) }),
    DOWN({ it.copy(y = it.y + 1) }),
    LEFT({ it.copy(x = it.x - 1) }),
    RIGHT({ it.copy(x = it.x + 1) })
}