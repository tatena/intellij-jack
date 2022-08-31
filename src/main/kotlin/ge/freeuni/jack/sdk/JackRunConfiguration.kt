package ge.freeuni.jack.sdk

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.io.exists
import ge.freeuni.jack.project.settings.JackProjectSettingsService
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


class JackRunConfiguration constructor(project: Project?, factory: ConfigurationFactory?, name: String?) :
    RunConfigurationBase<JackRunConfigurationOptions?>(project!!, factory, name) {
    override fun getOptions(): JackRunConfigurationOptions {
        return super.getOptions() as JackRunConfigurationOptions
    }

    var scriptName: String?
        get() = options.scriptName
        set(scriptName) {
            options.scriptName = scriptName
        }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration?> {
        return JackSettingsEditor()
    }

    override fun checkConfiguration() {}
    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState {
        val toolchain = project.getService(JackProjectSettingsService::class.java)?.toolchain
        val basePath = project.basePath
        val src = project.basePath + "/src"
        val out = project.basePath + "/out"
        val compiler = toolchain?.location.toString() + "/JackCompiler"

        val macCommands = listOf("sh", "$compiler.sh", out)

        fun deleteDirectory(file: File) {
            if (!file.exists()) {
                return
            }
            if (file.isDirectory) {
                val contents = file.listFiles()
                for (f in contents!!) {
                    deleteDirectory(f)
                }
            }
            file.delete()
        }

        fun copyFiles(from: File, to: Path) {
            if (!from.exists() || !to.exists()) {
                return
            }

            if (from.isDirectory) {
                val contents = from.listFiles()
                for (f in contents!!) {
                    copyFiles(f, to)
                }
            } else {
                if (from.toString().endsWith(".jack")) {
                    from.copyTo(File(from.toString().replace("/src/", "/out/")))
                }
            }
        }

        return object : CommandLineState(executionEnvironment) {
            @Throws(ExecutionException::class)
            override fun startProcess(): ProcessHandler {
                deleteDirectory(File("$basePath/out"))
                val outFile = Files.createDirectory(Paths.get("$basePath/out"))
                val srcFile = File(src)
                copyFiles(srcFile, outFile)

                val commandLine = GeneralCommandLine(macCommands)
                val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
                ProcessTerminatedListener.attach(processHandler)
                return processHandler
            }
        }
    }
}