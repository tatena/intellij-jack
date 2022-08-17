package ge.freeuni.jack.ide.newProject

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.Disposer
import java.io.File

class JackModuleBuilder : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> {
        return JackModuleType.INSTANCE;
    }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
    }

    override fun getCustomOptionsStep(context: WizardContext, parentDisposable: Disposable): ModuleWizardStep {
        return JackModuleWizardStep(context).apply {
            Disposer.register(parentDisposable, this::disposeUIResources)
        }
    }

    override fun createProject(name: String?, path: String?): Project? {
        val project = super.createProject(name, path) ?: return null
        val d = project.baseDir.createChildData(this, "Test.jack")
        File(d.path).writeText("class main {\n  \n}")
        return project
    }

}
