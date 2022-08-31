package ge.freeuni.jack.toolchain

import ge.freeuni.jack.toolchain.flavors.JackToolChainFlavor
import java.nio.file.Path

abstract class JackToolchainBase(val location: Path) {


    fun looksLikeValidToolchain(): Boolean = JackToolChainFlavor.getFlavor(location) != null

}