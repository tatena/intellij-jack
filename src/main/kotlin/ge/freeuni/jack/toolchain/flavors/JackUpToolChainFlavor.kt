package ge.freeuni.jack.toolchain.flavors

import java.nio.file.Path

class JackUpToolChainFlavor : JackToolChainFlavor() {

    override fun getHomePathCandidates(): Sequence<Path> {
        return emptySequence()
    }

}