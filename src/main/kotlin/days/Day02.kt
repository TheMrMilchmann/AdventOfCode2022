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
        val (a, b) = it.split(" ")
        a to b
    }

    fun part1(): Int {
        val rounds = data.map { (a, b) ->
            fun String.toMove() = when (this) {
                "A", "X" -> Move.Rock
                "B", "Y" -> Move.Paper
                "C", "Z" -> Move.Scissor
                else -> error("Unknown move: $this")
            }

            a.toMove() to b.toMove()
        }

        return rounds.sumOf { (opponent, reaction) ->
            val basePoints = when (reaction) {
                Move.Rock -> 1
                Move.Paper -> 2
                Move.Scissor -> 3
            }

            val resultPoints = when {
                Move.values()[(opponent.ordinal + 1) % 3] == reaction -> 6
                opponent == reaction -> 3
                else -> 0
            }

            basePoints + resultPoints
        }
    }

    println("Part 1: ${part1()} ")
}

private enum class Move {
    Rock,
    Paper,
    Scissor
}