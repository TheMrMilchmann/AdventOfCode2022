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
import java.math.BigInteger
import kotlin.math.*

fun main() {
    data class Point(val x: Long, val y: Long) {
        infix fun distanceTo(other: Point): Long = (abs(x - other.x) + abs(y - other.y))
    }

    data class Line(val a: Point, val b: Point) {

        private val mc by lazy {
            val l: Point
            val r: Point

            if (a.x <= b.x) {
                l = a
                r = b
            } else {
                l = b
                r = a
            }

            (r.y - l.y).sign
        }

        private val bc by lazy {
            (-(if (a.x <= b.x) a else b).y).also { println("bc: $it") }
        }

        infix fun Long.minusSafe(other: Long): Long {
            val res = this - other
            check(res < this == other > 0) { "Subtraction overflow: $this - $other" }
            return res
        }

        infix fun Long.timesSafe(other: Long): Long {
            val res = this * other
            check(other == 0L || res / other == this) { "Multiplication overflow: $this * $other" }
            return res
        }

        infix fun Long.timesSafe(other: Int): Long = timesSafe(other.toLong())

        operator fun contains(point: Point): Boolean {
            val dAX = a.x minusSafe point.x
            val dAY = a.y minusSafe point.y

            val dBX = b.x minusSafe point.x
            val dBY = b.y minusSafe point.y

            if ((dAX timesSafe dBY) minusSafe (dAY timesSafe dBX) == 0L) return false
            return point.x in minOf(a.x, b.x)..maxOf(a.x, b.x) && point.y in minOf(a.y, b.y)..maxOf(a.y, b.y)
        }

        infix fun intersect(other: Line): Point? {
//            if (mc == other.mc) return null
//
//            val x = (other.bc - bc).toDouble() / (mc - other.mc)
//            println("$x")
//
//            val y = mc * x + bc
//
//            return Point(x.toLong(), y.toLong()).also(::println)

            val abY = b.y minusSafe a.y
            val baX = a.x minusSafe b.x

            val cdY = other.b.y minusSafe other.a.y
            val dcX = other.a.x minusSafe other.b.x

            val det = (abY timesSafe dcX) minusSafe (cdY timesSafe baX)
            if (det == 0L) return null

            val eAB = BigInteger.valueOf((abY timesSafe a.x)) + BigInteger.valueOf((baX timesSafe a.y))
            val eCD = BigInteger.valueOf((cdY timesSafe other.a.x)) + BigInteger.valueOf((dcX timesSafe other.b.y))

            return Point(
                x = ((BigInteger.valueOf(dcX) * eAB - BigInteger.valueOf(baX) * eCD) / BigInteger.valueOf(det)).toLong(),
                y = ((BigInteger.valueOf(abY) * eCD - BigInteger.valueOf(cdY) * eAB) / BigInteger.valueOf(det)).toLong()
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

        fun getBorder(): Set<Point> = buildSet {
            for (i in -(range + 1)..range + 1) {
                val y = center.y + i
                val verticalDistance = abs(center.y - y)
                val horizontalDistance = (range + 1) - verticalDistance

                add(Point(center.x - horizontalDistance, y))
                add(Point(center.x + horizontalDistance, y))
            }
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

//        val lines = sequence {
//            yield(Line(Point(0, 0), Point(space, 0)))
//            yield(Line(Point(0, 0), Point(0, space)))
//            yield(Line(Point(space, 0), Point(space, space)))
//            yield(Line(Point(0, space), Point(space, space)))
//
//            scanAreas.forEach {
//                val t = it.center.copy(y = it.center.y - it.range)
//                val b = it.center.copy(y = it.center.y + it.range)
//                val l = it.center.copy(x = it.center.x - it.range)
//                val r = it.center.copy(x = it.center.x + it.range)
//
//                yield(Line(t, r))
//                yield(Line(r, b))
//                yield(Line(b, l))
//                yield(Line(l, t))
//            }
//        }
//
//        val (x, y) = lines.flatMap { a -> lines.mapNotNull { b -> a intersect b } }
//            .filter { (x, y) -> x in 0..space && y in 0..space }
//            .find { p -> scanAreas.none { it.center distanceTo p < it.range } }!!

        val (x, y) = scanAreas.flatMap { it.getBorder() }
            .toSet()
            .filter { (x, y) -> x in 0..space && y in 0..space }
            .find { p -> scanAreas.none { it.center distanceTo p <= it.range } }!!

        println("X: $x, Y: $y")
        return (x * space) + y
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}