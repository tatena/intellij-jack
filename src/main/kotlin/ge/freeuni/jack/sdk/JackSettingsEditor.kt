package ge.freeuni.jack.sdk

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JComponent
import javax.swing.JPanel



class JackSettingsEditor : SettingsEditor<JackRunConfiguration>() {
    private var myPanel: JPanel? = null
    private var myScriptName: LabeledComponent<TextFieldWithBrowseButton>? = null
    override fun resetEditorFrom(demoRunConfiguration: JackRunConfiguration) {
        myScriptName!!.component.setText(demoRunConfiguration.scriptName)
    }

    override fun applyEditorTo(jackRunConfiguration: JackRunConfiguration) {
        jackRunConfiguration.scriptName = myScriptName!!.component.text
    }

    override fun createEditor(): JComponent {
        return myPanel!!
    }

    private fun createUIComponents() {
        myScriptName = LabeledComponent()
        myScriptName!!.component = TextFieldWithBrowseButton()
    }
}
