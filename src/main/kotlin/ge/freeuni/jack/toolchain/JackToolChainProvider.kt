package ge.freeuni.jack.toolchain

import com.intellij.openapi.extensions.ExtensionPointName
import java.nio.file.Path

interface JackToolChainProvider {
    fun getToolchain(homePath: Path): JackToolchainBase?

    companion object {
        private val EP_NAME: ExtensionPointName<JackToolChainProvider> =
            ExtensionPointName.create("ge.freeuni.toolchainProvider")

        fun getToolchain(homePath: Path): JackToolchainBase? =
            EP_NAME.extensionList.asSequence()
                .mapNotNull { it.getToolchain(homePath) }
                .firstOrNull()
    }
}