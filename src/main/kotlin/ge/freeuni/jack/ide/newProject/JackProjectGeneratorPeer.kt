package ge.freeuni.jack.ide.newProject

import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.platform.GeneratorPeerImpl
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class JackProjectGeneratorPeer : GeneratorPeerImpl<ConfigurationData>() {

    private val newProjectPanel = JackNewProjectPanel { checkValid?.run() }
    private var checkValid: Runnable? = null

    override fun getSettings(): ConfigurationData = newProjectPanel.data

    override fun getComponent(myLocationField: TextFieldWithBrowseButton, checkValid: Runnable): JComponent {
        this.checkValid = checkValid
        return super.getComponent(myLocationField, checkValid)
    }

    override fun getComponent(): JComponent = panel {
        newProjectPanel.attachTo(this)
    }

    override fun validate(): ValidationInfo? = try {
//        newProjectPanel.validateSettings()
        null
    } catch (e: ConfigurationException) {
        ValidationInfo(e.message ?: "")
    }
}