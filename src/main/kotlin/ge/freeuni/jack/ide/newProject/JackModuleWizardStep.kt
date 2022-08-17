package ge.freeuni.jack.ide.newProject

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.AbstractWizard.isNewWizard
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.Disposer
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.intellij.util.ui.JBUI
import ge.freeuni.jack.ide.projectSettings.JackProjectSettingsPanel
import ge.freeuni.jack.pathAsPath
import ge.freeuni.jack.project.model.jackProjects
import ge.freeuni.jack.project.model.setup
import ge.freeuni.jack.project.settings.jackSettings
import java.awt.Panel
import javax.swing.JComponent
import javax.swing.JLabel


class JackModuleWizardStep(
    private val context: WizardContext,
    private val configurationUpdaterConsumer: ((ModuleBuilder.ModuleConfigurationUpdater) -> Unit)? = null
    ) : ModuleWizardStep() {

    private val newProjectPanel = JackNewProjectPanel()

    override fun getComponent(): JComponent = panel {
        newProjectPanel.attachTo(this)
    }.withBorderIfNeeded()

    override fun updateDataModel() {
        val data = newProjectPanel.data
        ConfigurationUpdater.data = data.settings

        val projectBuilder = context.projectBuilder
        if (projectBuilder is JackModuleBuilder) {
//            projectBuilder.configurationData = data
            projectBuilder.addModuleConfigurationUpdater(ConfigurationUpdater)
        } else {
            configurationUpdaterConsumer?.invoke(ConfigurationUpdater)
        }
    }

    override fun disposeUIResources() = Disposer.dispose(newProjectPanel)

    private object ConfigurationUpdater : ModuleBuilder.ModuleConfigurationUpdater() {
        var data: JackProjectSettingsPanel.Data? = null

        override fun update(module: Module, rootModel: ModifiableRootModel) {
            val data = data
            if (data != null) {
                module.project.jackSettings.modify {
                    it.toolchain = data.toolchain
//                    it.explicitPathToStdlib = data.explicitPathToStdlib
                }
            }
            // We don't use SDK, but let's inherit one to reduce the amount of
            // "SDK not configured" errors
            // https://github.com/intellij-rust/intellij-rust/issues/1062
            rootModel.inheritSdk()

            val contentEntry = rootModel.contentEntries.singleOrNull()
            if (contentEntry != null) {
//                val manifest = contentEntry.file?.findChild(CargoConstants.MANIFEST_FILE)
//                if (manifest != null) {
//                    module.project.jackProjects.attachCargoProject(manifest.pathAsPath)
//                }

                val projectRoot = contentEntry.file ?: return
                contentEntry.setup(projectRoot)
            }
        }
    }

    private fun <T : JComponent> T.withBorderIfNeeded(): T {
        if (isNewWizard()) {
            // border size is taken from `com.intellij.ide.wizard.NewProjectWizardStepPanel`
            border = JBUI.Borders.empty(14, 20)
        }
        return this
    }
}