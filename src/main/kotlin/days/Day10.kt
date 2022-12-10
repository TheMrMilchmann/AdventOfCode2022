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
    val data = readInput()

    fun part1(): Int =
        data.flatMap<_, (Int) -> Int> { line ->
            when (line) {
                "noop" -> listOf( { it })
                else -> listOf({ it }, { it + line.split(" ")[1].toInt() })
            }
        }.runningFold(initial = 1) { acc, it -> it(acc) }
            .withIndex()
            .filter { (index, _) -> (21 + index) % 40 == 0 }
            .sumOf { (index, value) -> (index + 1) * value }

    fun part2(): String =
        data.flatMap<_, (Int) -> Int> { line ->
            when (line) {
                "noop" -> listOf( { it })
                else -> listOf({ it }, { it + line.split(" ")[1].toInt() })
            }
        }.runningFold(initial = 1) { acc, it -> it(acc) }
            .take(240)
            .chunked(40)
            .map { row ->
                row.mapIndexed { index, value -> (index) in (value - 1)..(value + 1) }
            }
            .joinToString(separator = "\n") {
                it.joinToString(separator = "") { if (it) "█" else " " }
            }

    println("Part 1: ${part1()}")
    println("Part 2:\n${part2()}")
}