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

    val root = Directory("/", parent = null)
    var workingDir = root

    for (line in data) {
        if (line.startsWith("$")) {
            val segments = line.removePrefix("$ ").split(' ')
            val command = segments.first()

            when (command) {
                "cd" -> workingDir = workingDir.resolve(segments[1])
                "ls" -> {} // Ignore
                else -> error("Unknown command: $command")
            }
        } else {
            if (line.startsWith("dir")) {
                val name = line.removePrefix("dir ").trim()
                workingDir.createDirectory(name)
            } else {
                val (size, name) = line.split(' ')
                workingDir.createFile(name, size.toLong())
            }
        }
    }

//    println(root.printToString())

    println("Part 1: ${root.directorySequence().sumOf { if (it.size <= 100000) it.size else 0 }}")
}

private fun Directory.printToString(): String = buildString {
    appendLine("- $name (dir)")

    val children = children.values.joinToString(separator = "\n") {
        when (it) {
            is Directory -> it.printToString()
            is File -> "- ${it.name} (file, size=${it.size})"
        }
    }

    append(children.prependIndent("  "))
}

private sealed class Node {

    abstract val name: String
    abstract val size: Long

    abstract fun nodeSequence(): Sequence<Node>

}

private data class Directory(
    override val name: String,
    private val parent: Directory?
) : Node() {

    val children = mutableMapOf<String, Node>()

    private val root: Directory get() {
        var res = this
        while (res.parent != null) res = res.parent!!
        return res
    }

    override val size by lazy(LazyThreadSafetyMode.NONE) {
        children.values.sumOf(Node::size)
    }

    fun createDirectory(name: String): Directory {
        check(name !in children) { name }
        return Directory(name, parent = this).also { children[name] = it }
    }

    fun createFile(name: String, size: Long): File {
        check(name !in children)
        return File(name, size).also { children[name] = it }
    }

    override fun nodeSequence(): Sequence<Node> = sequence {
        yield(this@Directory)
        children.forEach { (_, node) -> yieldAll(node.nodeSequence()) }
    }

    fun directorySequence(): Sequence<Directory> =
        nodeSequence().filterIsInstance<Directory>()

    fun resolve(path: String): Directory = when (path) {
        ".." -> parent ?: error("Cannot resolve parent of root")
        "/" -> root
        else -> children.values.filterIsInstance<Directory>().find { it.name == path } ?: createDirectory(name)
    }

}

private data class File(
    override val name: String,
    override val size: Long
) : Node() {
    override fun nodeSequence(): Sequence<Node> = sequenceOf(this)
}