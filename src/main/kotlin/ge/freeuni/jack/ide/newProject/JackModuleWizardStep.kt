package ge.freeuni.jack.ide.newProject

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import javax.swing.JComponent
import javax.swing.JLabel


class JackModuleWizardStep : ModuleWizardStep() {


    override fun getComponent(): JComponent {
        return JLabel("Saba Magariaaaaaa");
    }

    override fun updateDataModel() {
        TODO("Not yet implemented")
    }
}