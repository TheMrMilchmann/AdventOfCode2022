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
    data class ParserState(val src: String, var offset: Int)

    fun parse(state: ParserState): Day13Node {
        if (state.src[state.offset] == '[') {
            state.offset++

            val elements = buildList {
                if (state.src[state.offset] != ']') {
                    add(parse(state))
                }

                while (state.src[state.offset++] == ',') {
                    add(parse(state))
                }
            }

            return Day13List(elements)
        } else {
            var element = state.src.drop(state.offset).takeWhile { it.isWhitespace() || it.isDigit() }
            state.offset += element.length

            element = element.trim()
            return Day13Int(element.toInt())
        }
    }

    val data = readInput().filter(String::isNotBlank).map { parse(ParserState(it, 0)) }

    fun part1(): Int =
        data.chunked(2) { (a, b) -> a to b }.withIndex().filter { (_, pair)  -> pair.first < pair.second }.sumOf { (index, _) -> index + 1 }

    fun part2(): Int {
        val divider2 = Day13List(Day13List(listOf(Day13Int(2))))
        val divider6 = Day13List(Day13List(listOf(Day13Int(6))))

        return buildList {
            addAll(data)
            add(divider2)
            add(divider6)
        }
            .also { println(it.indexOf(divider2)) }
            .sortedWith(Day13Node::compareTo)
            .withIndex()
            .map { (i, packet) -> if (packet == divider2 || packet == divider6) i + 1 else 1 }
            .reduce(Int::times)
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}


private operator fun Day13Node.compareTo(other: Day13Node): Int {
    return if (this is Day13Int && other is Day13Int) {
        this.value.compareTo(other.value)
    } else {
        val a = (this as? Day13List)?.elements ?: listOf(this)
        val b = (other as? Day13List)?.elements ?: listOf(other)
        a.compareTo(b)
    }
}

private operator fun List<Day13Node>.compareTo(other: List<Day13Node>): Int {
    val thisItr = iterator()
    val otherItr = other.iterator()

    while (thisItr.hasNext()) {
        if (!otherItr.hasNext()) return 1

        val a = thisItr.next()
        val b = otherItr.next()

        val res = a.compareTo(b)
        if (res != 0) return res
    }

    return if (otherItr.hasNext()) -1 else 0
}

private sealed class Day13Node

private data class Day13Int(
    val value: Int
) : Day13Node()

private data class Day13List(
    val elements: List<Day13Node>
) : Day13Node(), List<Day13Node> by elements