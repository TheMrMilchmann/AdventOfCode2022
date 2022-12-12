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
import java.util.*

fun main() {
    val grid = readInput().map(String::toList).toGrid()
    val (startingPos) = grid.positions.zip(grid).single { (_, v) -> v == 'S' }
    val (targetPos) = grid.positions.zip(grid).single { (_, v) -> v == 'E' }

    fun Grid<Char>.part1(): Int {
        fun flatIndexOf(pos: GridPos) =
            (pos.y.intValue * width) + pos.x.intValue

        val steps = IntArray(grid.positions.size) { Int.MAX_VALUE }
        steps[flatIndexOf(startingPos)] = 0

        val queue = PriorityQueue<Pair<GridPos, Int>>(compareBy { (_, priority) -> priority })
        queue.add(startingPos to 0)

        while (queue.isNotEmpty()) {
            val (pos, _) = queue.remove()
            val index = flatIndexOf(pos)
            if (pos == targetPos) return steps[index]

            for (vPos in getAdjacentPositions(pos)) {
                val vIndex = flatIndexOf(vPos)

                fun Char.toElevation() = when (this) {
                    'E' -> 'z'
                    'S' -> 'a'
                    else -> this
                }

                val alt = steps[index] + 1

                if (alt < steps[vIndex] && this[pos].toElevation() + 1 >= this[vPos].toElevation()) {
                    steps[vIndex] = alt
                    queue.add(vPos to alt)
                }
            }
        }

        error("Step calculation aborted unexpectedly")
    }

    println("Part 1: ${grid.part1()}")
}