package ge.freeuni.jack.toolchain.flavors

import com.intellij.openapi.util.SystemInfo
import ge.freeuni.jack.toPath
import java.nio.file.Path

class JackMacToolChainFlavor : JackToolChainFlavor() {

    override fun getHomePathCandidates(): Sequence<Path> {
        return emptySequence()
    }

    override fun isApplicable(): Boolean = SystemInfo.isMac
}