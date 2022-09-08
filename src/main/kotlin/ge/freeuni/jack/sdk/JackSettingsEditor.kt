package ge.freeuni.jack.sdk

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JComponent
import javax.swing.JPanel



class JackSettingsEditor : SettingsEditor<JackRunConfiguration>() {
    private var myPanel: JPanel? = null
    override fun resetEditorFrom(demoRunConfiguration: JackRunConfiguration) {
    }

    override fun applyEditorTo(jackRunConfiguration: JackRunConfiguration) {
    }

    override fun createEditor(): JComponent {
        return myPanel!!
    }

    private fun createUIComponents() {
    }
}
