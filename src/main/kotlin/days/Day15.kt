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
    data class Point(val x: Long, val y: Long) {
        infix fun distanceTo(other: Point): Long = (abs(x - other.x) + abs(y - other.y))
    }

    data class Line(val a: Point, val b: Point) {

        infix fun intersect(other: Line): Point? {
            val abX = a.x - b.x
            val abY = a.y - b.y
            val acX = a.x - other.a.x
            val acY = a.y - other.a.y
            val cdX = other.a.x - other.b.x
            val cdY = other.a.y - other.b.y

            val t = (acX.toDouble() * cdY - acY * cdX) / (abX * cdY - abY * cdX)
            val u = (acX.toDouble() * abY - acY * abX) / (abX * cdY - abY * cdX)
            if (t < 0 || 1 < t || u < 0 || 1 < u) return null

            return Point(
                x = (a.x + t * (b.x - a.x)).toLong(),
                y = (a.y + t * (b.y - a.y)).toLong()
            )
        }

    }

    data class ScanArea(
        val center: Point,
        val beacon: Point,
        val range: Long = center distanceTo beacon
    ) {

        fun getScansOnRow(row: Long): LongRange {
            val verticalDistance = abs(center.y - row)
            if (verticalDistance > range) return LongRange.EMPTY

            val horizontalDistance = range - verticalDistance
            return (center.x - horizontalDistance)..(center.x + horizontalDistance)
        }

    }

    val data = readInput().map {
        val (sX, sY, bX, bY) = """(-)?\d+""".toRegex().findAll(it).take(4).toList()
        Point(sX.value.toLong(), sY.value.toLong()) to Point(bX.value.toLong(), bY.value.toLong())
    }

    val scanAreas = data.map { (sensor, beacon) -> ScanArea(sensor, beacon) }

    fun part1(): Int {
        val row = 2_000_000L

        val beacons = scanAreas.mapNotNull { if (it.beacon.y == row) it.beacon.x else null }.toSet()
        return scanAreas.flatMap { it.getScansOnRow(row) }.filter { it !in beacons }.toSet().size
    }

    fun part2(): Long {
        val space = 4_000_000L

        val lines = sequence {
            yield(Line(Point(0, 0), Point(space, 0)))
            yield(Line(Point(0, 0), Point(0, space)))
            yield(Line(Point(space, 0), Point(space, space)))
            yield(Line(Point(0, space), Point(space, space)))

            scanAreas.forEach {
                val t = it.center.copy(y = it.center.y - it.range - 1)
                val b = it.center.copy(y = it.center.y + it.range + 1)
                val l = it.center.copy(x = it.center.x - it.range - 1)
                val r = it.center.copy(x = it.center.x + it.range + 1)

                yield(Line(t, r))
                yield(Line(r, b))
                yield(Line(b, l))
                yield(Line(l, t))
            }
        }

        val (x, y) = lines.flatMap { a -> lines.mapNotNull { b -> a intersect b } }
            .filter { (x, y) -> x in 0..space && y in 0..space }
            .find { p -> scanAreas.none { it.center distanceTo p <= it.range } }!!

        return (x * space) + y
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}