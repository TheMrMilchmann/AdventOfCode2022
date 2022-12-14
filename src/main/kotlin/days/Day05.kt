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
    data class Move(val amount: Int, val fromIndex: Int, val toIndex: Int)

    val stacks = Array(9) { ArrayDeque<Char>() }
    val moves: List<Move>

    readInput().let { lines ->
        val initialState = lines.takeWhile { it.isNotBlank() }
        initialState.forEach { line ->
            stacks.forEachIndexed { index, stack ->
                val char = line[1 + index * 4]
                if (char in 'A'..'Z') stack.addFirst(char)
            }
        }

        moves = lines.drop(initialState.size).mapNotNull {
            val (amount, from, to) = "move (\\d+) from (\\d) to (\\d)".toRegex().matchEntire(it)?.destructured ?: return@mapNotNull null
            Move(amount = amount.toInt(), fromIndex = from.toInt() - 1, toIndex = to.toInt() - 1)
        }
    }

    fun part1(): String {
        @Suppress("NAME_SHADOWING")
        val stacks = Array(9) { ArrayDeque(stacks[it]) }

        for (move in moves) {
            for (i in 0 until move.amount) {
                val it = stacks[move.fromIndex].removeLast()
                stacks[move.toIndex].addLast(it)
            }
        }

        return stacks.joinToString(separator = "") { it.last().toString() }
    }

    fun part2(): String {
        @Suppress("NAME_SHADOWING")
        val stacks = Array(9) { ArrayDeque(stacks[it]) }

        for (move in moves) {
            val its = buildList {
                for (i in 0 until move.amount) {
                    add(0, stacks[move.fromIndex].removeLast())
                }
            }

            for (it in its) {
                stacks[move.toIndex].addLast(it)
            }
        }

        return stacks.joinToString(separator = "") { it.last().toString() }
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}