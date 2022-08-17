package ge.freeuni.jack.ide.newProject


import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import ge.freeuni.jack.language.JackIcons
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull

import javax.swing.*

class JackModuleType : ModuleType<JackModuleBuilder>(ID)
{
    override fun getNodeIcon(isOpened: Boolean): Icon = JackIcons.FILE

    override fun createModuleBuilder(): JackModuleBuilder = JackModuleBuilder()

    override fun getDescription(): String = "Jack module"

    override fun getName(): String = "Jack"

    companion object {
        const val ID = "JACK_MODULE"
        val INSTANCE: JackModuleType by lazy { ModuleTypeManager.getInstance().findByID(ID) as JackModuleType }
    }

}