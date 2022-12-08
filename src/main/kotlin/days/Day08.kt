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

fun main() {
    val grid = readInput().map { it.toCharArray().map(Char::digitToInt) }.toGrid()

    fun part1(): Int {
        var count = 0

        for (y in grid.verticalIndices) {
            for (x in grid.horizontalIndices) {
                if (grid.beam(GridPos(x, y), grid::shiftUp).all { grid[it] < grid[x, y] }
                    || grid.beam(GridPos(x, y), grid::shiftDown).all { grid[it] < grid[x, y] }
                    || grid.beam(GridPos(x, y), grid::shiftLeft).all { grid[it] < grid[x, y] }
                    || grid.beam(GridPos(x, y), grid::shiftRight).all { grid[it] < grid[x, y] }
                ) {
                    count++
                }
            }
        }

        return count
    }

    fun part2() =
        grid.positions.maxOfOrNull { pos ->
            listOf(
                grid.beam(pos, grid::shiftUp),
                grid.beam(pos, grid::shiftDown),
                grid.beam(pos, grid::shiftLeft),
                grid.beam(pos, grid::shiftRight)
            ).map { beam ->
                val res = beam.takeWhile { grid[it] < grid[pos] }.count()
                if (res == beam.size) res else res + 1
            }.reduce { acc, i -> acc * i }
        }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}