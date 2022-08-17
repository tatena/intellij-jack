package ge.freeuni.jack.ide.newProject

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class JackModuleBuilder : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> {
        return JackModuleType.INSTANCE;
    }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep {
        return JackModuleWizardStep()
    }
}
