package ge.freeuni.jack.toolchain.flavors

import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import ge.freeuni.jack.hasExecutable
import java.nio.file.Path

class JackUpToolChainFlavor : JackToolChainFlavor() {

    override fun getHomePathCandidates(): Sequence<Path> {
        return emptySequence()
    }

    override fun isValidToolchainPath(path: Path): Boolean =
        super.isValidToolchainPath(path)
}