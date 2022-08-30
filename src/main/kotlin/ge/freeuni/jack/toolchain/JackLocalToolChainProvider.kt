package ge.freeuni.jack.toolchain

import com.intellij.execution.wsl.WslPath
import com.intellij.openapi.util.SystemInfo
import ge.freeuni.jack.toolchain.wsl.JackLocalToolchain
import java.nio.file.Path

class JackLocalToolChainProvider : JackToolChainProvider {
    override fun getToolchain(homePath: Path): JackToolchainBase? {
        return JackLocalToolchain(homePath)
    }
}
