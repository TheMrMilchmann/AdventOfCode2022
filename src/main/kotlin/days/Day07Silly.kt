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
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.streams.asSequence

fun main() {
    val input = readInput()

    val root = Path.of("T:\\AoC7Silly") // <- hardcoded because this is just a silly PoC
    var path = root

    for (line in input) {
        if (line.startsWith("$")) {
            val command = line.removePrefix("$ ")

            if (command.startsWith("cd")) {
                val target = command.removePrefix("cd ").trim()

                path = when (target) {
                    "/" -> root
                    else -> path.resolve(target)
                }
            }
        } else {
            if (line.startsWith("dir")) continue

            val (size, name) = line.split(' ')
            Files.createDirectories(path)
            Files.write(path.resolve(name), ByteArray(size.toInt()))
        }
    }

    fun Path.size(): Long =
        if (isDirectory()) listDirectoryEntries().sumOf(Path::size) else fileSize()

    fun part1(): Long =
        Files.walk(root).asSequence().sumOf {
            val size = if (it.isDirectory()) it.size() else 0
            if (size <= 100_000) size else 0
        }

    fun part2(): Long {
        val minSizeToDelete = 30_000_000L - (70_000_000L - root.size())
        return Files.walk(root)
            .asSequence()
            .mapNotNull {
                val size = it.size()
                if (it.isDirectory() && size >= minSizeToDelete) size else null
            }
            .min()
    }

    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}