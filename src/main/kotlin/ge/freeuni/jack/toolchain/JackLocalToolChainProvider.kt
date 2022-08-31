package ge.freeuni.jack.toolchain

import ge.freeuni.jack.toolchain.wsl.JackLocalToolchain
import java.nio.file.Path

class JackLocalToolChainProvider : JackToolChainProvider {
    override fun getToolchain(homePath: Path): JackToolchainBase {
        return JackLocalToolchain(homePath)
    }
}
