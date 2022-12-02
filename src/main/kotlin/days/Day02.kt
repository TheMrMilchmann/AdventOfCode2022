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

    fun String.toMove() = when (this) {
        "A", "X" -> Move.Rock
        "B", "Y" -> Move.Paper
        "C", "Z" -> Move.Scissor
        else -> error("Unknown move: $this")
    }

    fun part1(): Int {
        val rounds = data.map { (a, b) ->
            a.toMove() to b.toMove()
        }

        return rounds.sumOf { (opponent, reaction) ->
            val result = Result.values().find { reaction == opponent.getReactionFor(it) }!!
            reaction.points + result.points
        }
    }

    fun part2(): Int {
        val rounds = data.map { (a, b) ->
            val result = when (b) {
                "X" -> Result.Defeat
                "Y" -> Result.Draw
                "Z" -> Result.Victory
                else -> error("Unknown result: $b")
            }

            a.toMove() to result
        }

        return rounds.sumOf { (opponent, result) -> result.points + opponent.getReactionFor(result).points }
    }

    println("Part 1: ${part1()} ")
    println("Part 2: ${part2()} ")
}

private enum class Move(val points: Int) {
    Rock(1),
    Paper(2),
    Scissor(3);

    fun getReactionFor(result: Result): Move =
        result.selectReaction(this)

    companion object {
        val cachedValues = values()
    }
}

private enum class Result(val points: Int, val selectReaction: (Move) -> Move) {
    Defeat(0, { Move.cachedValues[(it.ordinal + 2) % 3] }),
    Draw(3, { it }),
    Victory(6, { Move.cachedValues[(it.ordinal + 1) % 3] })
}