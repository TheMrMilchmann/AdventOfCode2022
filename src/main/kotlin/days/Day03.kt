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
    val data = readInput().map {
        val a = it.substring(0, it.length / 2).toSet()
        val b = it.substring(it.length / 2).toSet()

        a to b
    }

    fun Char.toPriority(): Int = when {
        this >= 'a' -> (this - 'a') + 1
        else -> 27 + (this - 'A')
    }

    fun part1(): Int {
        return data.sumOf { (a, b) -> a.single { it in b }.toPriority() }
    }

    fun part2(): Int {
        return data.map { (a, b) -> a + b }
            .chunked(3)
            .sumOf { it.reduce { acc, set -> acc.filter { it in set }.toSet() }.single().toPriority() }
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}