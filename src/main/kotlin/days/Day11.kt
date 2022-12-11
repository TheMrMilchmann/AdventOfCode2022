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
    class Monkey(
        val startingItems: List<Long>,
        val operation: (Long) -> Long,
        val testDivisor: Int,
        val findTarget: (Long) -> Int
    )

    val monkeys = readInput().asSequence()
        .filter { it.isNotBlank() }
        .chunked(size = 6) { lines ->
            val startingItems = lines[1].trim().removePrefix("Starting items: ").split(", ").map(String::toLong)
            val operation: (Long) -> Long = lines[2].trim().removePrefix("Operation: new = old ").let { src ->
                val op: Long.(Long) -> Long = when (src[0]) {
                    '+' -> Long::plus
                    '*' -> Long::times
                    else -> error("Unknown operation: $src")
                }

                when (val value = src.substring(startIndex = 2)) {
                    "old" -> {{ it.op(it) }}
                    else -> {{ it.op(value.toLong()) }}
                }
            }

            val testDivisor = lines[3].substringAfterLast(' ').toInt()
            val testPositiveTarget = lines[4].substringAfterLast(' ').toInt()
            val testNegativeTarget = lines[5].substringAfterLast(' ').toInt()

            val findTarget: (Long) -> Int = { if (it % testDivisor == 0L) testPositiveTarget else testNegativeTarget }

            Monkey(
                startingItems,
                operation,
                testDivisor,
                findTarget
            )
        }.toList()

    fun solve(rounds: Int, reduceWorryLevel: (Long) -> Long): Long {
        val inspections = IntArray(monkeys.size)
        val inventories = List(monkeys.size) { ArrayList(monkeys[it].startingItems) }

        repeat(rounds) {
            monkeys.forEachIndexed { index, monkey ->
                val items = inventories[index]
                inspections[index] += items.size

                val itr = items.iterator()

                while (itr.hasNext()) {
                    var item = itr.next()
                    itr.remove()

                    item = monkey.operation(item)
                    item = reduceWorryLevel(item)

                    val target = monkey.findTarget(item)
                    inventories[target].add(item)
                }
            }
        }

        return inspections.sortedDescending().take(2).map(Int::toLong).reduce(Long::times)
    }

    println("Part 1: ${solve(20) { it / 3 }}")

    val base = monkeys.map(Monkey::testDivisor).reduce(Int::times)
    println("Part 2: ${solve(10_000) { it % base }}")
}