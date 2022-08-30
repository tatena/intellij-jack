package ge.freeuni.jack

import com.intellij.ide.util.PsiNavigationSupport
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths


fun String.toPathOrNull(): Path? = pathOrNull(this::toPath)

private inline fun pathOrNull(block: () -> Path): Path? {
    return try {
        block()
    } catch (e: InvalidPathException) {
        null
    }
}

fun String.toPath(): Path = Paths.get(this)

val VirtualFile.pathAsPath: Path get() = Paths.get(path)

fun Path.resolveOrNull(other: String): Path? = pathOrNull { resolve(other) }

