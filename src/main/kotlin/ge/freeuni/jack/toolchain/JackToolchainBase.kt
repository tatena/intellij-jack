package ge.freeuni.jack.toolchain

import com.intellij.execution.wsl.WslPath
import ge.freeuni.jack.toolchain.flavors.JackToolChainFlavor
import ge.freeuni.jack.toolchain.wsl.getHomePathCandidates
import java.nio.file.Path

abstract class JackToolchainBase(val location: Path) {

    companion object {

        @JvmOverloads
        fun suggest(projectDir: Path? = null): JackToolchainBase? {
            val distribution = projectDir?.let { WslPath.getDistributionByWindowsUncPath(it.toString()) }
            val toolchain = distribution
                ?.getHomePathCandidates()
                ?.filter { JackToolChainFlavor.getFlavor(it) != null }
                ?.mapNotNull { JackToolChainProvider.getToolchain(it.toAbsolutePath()) }
                ?.firstOrNull()
            if (toolchain != null) return toolchain

            return JackToolChainFlavor.getApplicableFlavors()
                .asSequence()
                .flatMap { it.suggestHomePaths() }
                .mapNotNull { JackToolChainProvider.getToolchain(it.toAbsolutePath()) }
                .firstOrNull()
        }
    }
}