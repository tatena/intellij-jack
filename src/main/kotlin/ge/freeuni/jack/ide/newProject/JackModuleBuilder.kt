package ge.freeuni.jack.ide.newProject

import com.intellij.execution.RunManager
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.Disposer
import ge.freeuni.jack.pathAsPath
import ge.freeuni.jack.sdk.JackConfigurationFactory
import ge.freeuni.jack.sdk.JackRunConfiguration
import ge.freeuni.jack.sdk.JackRunConfigurationType
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class JackModuleBuilder : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> {
        return JackModuleType.INSTANCE
    }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val root = doAddContentEntry(modifiableRootModel)?.file ?: return

        println(root)

        val root1 = root.pathAsPath

        val directory = "$root1/src"
        val fileName = "$directory/Main.jack"
        Files.createDirectory(Paths.get(directory))

        val file = File(fileName)
        file.writeText("class Main {\n\tfunction void main() {\n\t\tdo Output.println(\"hello world\");\n\t}\n}")

//        val runManager = RunManager.getInstance(modifiableRootModel.project)
//        runManager.addConfiguration(JackConfigurationFactory().createTemplateConfiguration(modifiableRootModel.project))
//        runManager.selectedConfiguration = configuration
    }

    override fun getCustomOptionsStep(context: WizardContext, parentDisposable: Disposable): ModuleWizardStep {
        return JackModuleWizardStep(context).apply {
            Disposer.register(parentDisposable, this::disposeUIResources)
        }
    }

}
