package ge.freeuni.jack.toolchain.flavors

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.util.io.isDirectory
import java.nio.file.Path


abstract class JackToolChainFlavor {

    fun suggestHomePaths(): Sequence<Path> = getHomePathCandidates().filter { isValidToolchainPath(it) }

    protected abstract fun getHomePathCandidates(): Sequence<Path>


    protected open fun isApplicable(): Boolean = true


    protected open fun isValidToolchainPath(path: Path): Boolean {
        return path.isDirectory()
//                &&
//                hasExecutable(path, Rustc.NAME) &&
//                hasExecutable(path, Cargo.NAME)
    }

//    protected open fun hasExecutable(path: Path, toolName: String): Boolean = path.hasExecutable(toolName)
//
//    protected open fun pathToExecutable(path: Path, toolName: String): Path = path.pathToExecutable(toolName)

    companion object {
        private val EP_NAME: ExtensionPointName<JackToolChainFlavor> =
            ExtensionPointName.create("ge.freeuni.toolchainFlavor")

        fun getApplicableFlavors(): List<JackToolChainFlavor> =
            EP_NAME.extensionList.filter { it.isApplicable() }

        fun getFlavor(path: Path): JackToolChainFlavor? =
            getApplicableFlavors().find { flavor -> flavor.isValidToolchainPath(path) }
    }
}
