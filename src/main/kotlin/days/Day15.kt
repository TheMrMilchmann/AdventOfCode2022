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
    data class Point(val x: Int, val y: Int) {
        infix fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)
    }

    data class ScanArea(
        val center: Point,
        val beacon: Point,
        val range: Int = center distanceTo beacon
    ) {

        fun getScansOnRow(row: Int): IntRange {
            val verticalDistance = abs(center.y - row)
            if (verticalDistance > range) return IntRange.EMPTY

            val horizontalDistance = range - verticalDistance
            return (center.x - horizontalDistance)..(center.x + horizontalDistance)
        }

    }

    val data = readInput().map {
        val (sX, sY, bX, bY) = """(-)?\d+""".toRegex().findAll(it).take(4).toList()
        Point(sX.value.toInt(), sY.value.toInt()) to Point(bX.value.toInt(), bY.value.toInt())
    }

    val scanAreas = data.map { (sensor, beacon) -> ScanArea(sensor, beacon) }

    fun part1(): Int {
        val row = 2_000_000

        val beacons = scanAreas.mapNotNull { if (it.beacon.y == row) it.beacon.x else null }.toSet()
        return scanAreas.flatMap { it.getScansOnRow(row) }.filter { it !in beacons }.toSet().size
    }

    println("Part 1: ${part1()}")
}