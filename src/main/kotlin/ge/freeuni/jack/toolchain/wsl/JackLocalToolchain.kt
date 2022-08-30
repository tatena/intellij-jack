package ge.freeuni.jack.toolchain.wsl

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import ge.freeuni.jack.toolchain.JackToolchain
import ge.freeuni.jack.toolchain.JackToolchainBase
import java.io.File
import java.nio.file.Path

open class JackLocalToolchain(location: Path) : JackToolchainBase(location) {
//    override val fileSeparator: String get() = File.separator
//
//    override val executionTimeoutInMilliseconds: Int = 1000
//
//    override fun patchCommandLine(commandLine: GeneralCommandLine): GeneralCommandLine = commandLine
//
//    override fun toLocalPath(remotePath: String): String = remotePath
//
//    override fun toRemotePath(localPath: String): String = localPath
//
//    override fun expandUserHome(remotePath: String): String = FileUtil.expandUserHome(remotePath)
//
//    override fun getExecutableName(toolName: String): String = if (SystemInfo.isWindows) "$toolName.exe" else toolName
//
//    override fun pathToExecutable(toolName: String): Path = location.pathToExecutable(toolName)
//
//    override fun hasExecutable(exec: String): Boolean = location.hasExecutable(exec)
//
//    override fun hasCargoExecutable(exec: String): Boolean = pathToCargoExecutable(exec).isExecutable()
}