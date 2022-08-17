package ge.freeuni.jack.toolchain

import ge.freeuni.jack.toolchain.wsl.JackLocalToolchain
import java.nio.file.Path

class JackToolchain(location: Path) : JackLocalToolchain(location) {
    companion object {
        fun suggest(): JackToolchain? = JackToolchainBase.suggest()?.let(::from)

        fun from(newToolchain: JackToolchainBase): JackToolchain? =
            if (newToolchain is JackLocalToolchain) JackToolchain(newToolchain.location) else null
    }
}