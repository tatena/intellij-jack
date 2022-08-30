package ge.freeuni.jack

import java.io.File
import java.nio.file.Path

val Path.systemIndependentPath: String
    get() = toString().replace(File.separatorChar, '/')