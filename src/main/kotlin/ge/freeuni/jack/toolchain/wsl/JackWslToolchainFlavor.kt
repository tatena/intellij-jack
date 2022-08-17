package ge.freeuni.jack.toolchain.wsl

import com.intellij.execution.wsl.WSLDistribution
import com.intellij.execution.wsl.WSLUtil
import com.intellij.execution.wsl.WslDistributionManager
import com.intellij.execution.wsl.WslPath
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.NlsContexts
import com.intellij.util.io.isDirectory
import ge.freeuni.jack.resolveOrNull
import ge.freeuni.jack.toolchain.flavors.JackToolChainFlavor
import java.nio.file.Path

class JackWslToolchainFlavor : JackToolChainFlavor() {

    override fun getHomePathCandidates(): Sequence<Path> = sequence {
        val distributions = compute("Getting installed distributions...") {
            WslDistributionManager.getInstance().installedDistributions
        }
        for (distro in distributions) {
            yieldAll(distro.getHomePathCandidates())
        }
    }

    override fun isApplicable(): Boolean =
        WSLUtil.isSystemCompatible()

    override fun isValidToolchainPath(path: Path): Boolean = true
//       WslPath.isWslUncPath(path.toString()) && super.isValidToolchainPath(path)

//    fun hasExecutable(path: Path, toolName: String): Boolean = true
//        path.hasExecutableOnWsl(toolName)

//    override fun pathToExecutable(path: Path, toolName: String): Path  = path.pathToExecutableOnWsl(toolName)
}

fun WSLDistribution.getHomePathCandidates(): Sequence<Path> = sequence {
    @Suppress("UnstableApiUsage")
    val root = uncRootPath
    val environment = compute("Getting environment variables...") { environment }
    if (environment != null) {
        val home = environment["HOME"]
        val remoteCargoPath = home?.let { "$it/.cargo/bin" }
        val localCargoPath = remoteCargoPath?.let { root.resolve(it) }
        if (localCargoPath?.isDirectory() == true) {
            yield(localCargoPath)
        }

        val sysPath = environment["PATH"]
        for (remotePath in sysPath.orEmpty().split(":")) {
            if (remotePath.isEmpty()) continue
            val localPath = root.resolveOrNull(remotePath) ?: continue
            if (!localPath.isDirectory()) continue
            yield(localPath)
        }
    }

    for (remotePath in listOf("/usr/local/bin", "/usr/bin")) {
        val localPath = root.resolve(remotePath)
        if (!localPath.isDirectory()) continue
        yield(localPath)
    }
}

private fun <T> compute(
    @Suppress("UnstableApiUsage") @NlsContexts.ProgressTitle title: String,
    getter: () -> T
): T = if (isDispatchThread) {
    val project = ProjectManager.getInstance().defaultProject
    project.computeWithCancelableProgress(title, getter)
} else {
    getter()
}


fun Path.pathToExecutableOnWsl(toolName: String): Path = resolve(toolName)
val isDispatchThread: Boolean get() = ApplicationManager.getApplication().isDispatchThread
fun <T> Project.computeWithCancelableProgress(
    @Suppress("UnstableApiUsage") @NlsContexts.ProgressTitle title: String,
    supplier: () -> T
): T {
    if (isUnitTestMode) {
        return supplier()
    }
    return ProgressManager.getInstance().runProcessWithProgressSynchronously<T, Exception>(supplier, title, true, this)
}
val isUnitTestMode: Boolean get() = ApplicationManager.getApplication().isUnitTestMode
