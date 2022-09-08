package ge.freeuni.jack.ide.newProject

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.io.exists
import ge.freeuni.jack.pathAsPath
import ge.freeuni.jack.project.settings.JackProjectSettingsService
import ge.freeuni.jack.sdk.JackCommandConfigurationType
import ge.freeuni.jack.sdk.JackConfigurationFactory
import ge.freeuni.jack.sdk.JackRunConfiguration
import ge.freeuni.jack.sdk.JackRunConfigurationType
import kotlinx.serialization.json.Json.Default.configuration
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class JackModuleBuilder : ModuleBuilder() {

    override fun getModuleType(): ModuleType<*> {
        return JackModuleType.INSTANCE
    }

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
                from.copyTo(File(from.toString().replace("src", "out")))
            }
        }
    }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val root = doAddContentEntry(modifiableRootModel)?.file ?: return
        val root1 = root.pathAsPath

        val directory = "$root1/src"
        val fileName = "$directory/Main.jack"
        Files.createDirectory(Paths.get(directory))

        val file = File(fileName)
        file.writeText(
            "class Main {\n\tfunction void main() {\n\t\tdo Output.printString(\"hello world\");\n\t\treturn;\n" +
                    "\t}\n}"
        )
        val project = modifiableRootModel.project
        val toolchain = project.getService(JackProjectSettingsService::class.java)?.toolchain
        val basePath = project.basePath
        val src = project.basePath + "/src"
        val out = project.basePath + "/out"
        val compiler = toolchain?.location.toString() + "/JackCompiler"

        val macCommands = listOf("sh", "$compiler.sh", out)
        val windowsCommands = listOf("$compiler.bat", out)
        val runManager = RunManager.getInstance(project)
        val configuration = runManager.createConfiguration("Main", JackCommandConfigurationType.getInstance().factory).apply {
            deleteDirectory(File("$basePath/out"))
            val outFile = Files.createDirectory(Paths.get("$basePath/out"))
            val srcFile = File(src)
            copyFiles(srcFile, outFile)

            val commandLine = if (SystemInfo.isWindows) {
                GeneralCommandLine(windowsCommands)
            } else {
                GeneralCommandLine(macCommands)
            }
            val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
            ProcessTerminatedListener.attach(processHandler)
        }

        runManager.addConfiguration(configuration)
        runManager.selectedConfiguration = configuration
    }

    override fun getCustomOptionsStep(context: WizardContext, parentDisposable: Disposable): ModuleWizardStep {
        return JackModuleWizardStep(context).apply {
            Disposer.register(parentDisposable, this::disposeUIResources)
        }
    }

}
