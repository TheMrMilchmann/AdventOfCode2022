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
    data class Point(val x: Int, val y: Int)
    data class Line(val start: Point, val end: Point)

    val lines = readInput().flatMap { path ->
        path.split(" -> ")
            .map {
                val (x, y) = it.split(",")
                Point(x.toInt(), y.toInt())
            }
            .zipWithNext()
            .map { (start, end) -> Line(start, end) }
    }

    var maxX = 0
    var maxY = 0

    for (line in lines) {
        maxX = maxOf(maxX, line.start.x, line.end.x)
        maxY = maxOf(maxY, line.start.y, line.end.y)
    }

    val width = maxOf(maxX, maxY + 502)
    val height = maxY + 1

    data class GridElement(
        var isOccupied: Char = '.'
    )

    val grid = Grid(width, height) { GridElement() }

    for (line in lines) {
        if (line.start.y == line.end.y) {
            for (x in minOf(line.start.x, line.end.x)..maxOf(line.start.x, line.end.x)) {
                grid[x.hPos, line.start.y.vPos].isOccupied = '#'
            }
        } else if (line.start.x == line.end.x) {
            for (y in minOf(line.start.y, line.end.y)..maxOf(line.start.y, line.end.y)) {
                grid[line.start.x.hPos, y.vPos].isOccupied = '#'
            }
        } else {
            error("Unexpected line: $line")
        }
    }

    fun fall(grid: Grid<GridElement>, startX: Int = 500, startY: Int = 0): Point {
        var x = startX.hPos
        var y = startY.vPos

        loop@while (y.intValue < maxY) {
            val ny = y + 1

            when {
                grid[x, ny].isOccupied == '.' -> {}
                grid[x - 1, ny].isOccupied == '.' -> x -= 1
                grid[x + 1, ny].isOccupied == '.' -> x += 1
                else -> break@loop
            }

            y = ny
        }

        grid[x, y].isOccupied = '+'
        return Point(x.intValue, y.intValue)
    }

    fun part1(): Int {
        val g = grid.map { _, it -> it.copy() }

        return generateSequence { fall(g) }
            .takeWhile { it.y < maxY }
            .count()
    }

    println("Part 1: ${part1()}")
}