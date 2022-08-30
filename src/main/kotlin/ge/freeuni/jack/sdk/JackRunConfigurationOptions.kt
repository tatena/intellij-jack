package ge.freeuni.jack.sdk

import com.intellij.execution.configurations.RunConfigurationOptions
import com.intellij.openapi.components.StoredProperty


class JackRunConfigurationOptions : RunConfigurationOptions() {
    private val myScriptName: StoredProperty<String?> =
        string("").provideDelegate(this, "scriptName")

    var scriptName
        get() = myScriptName.getValue(this)
        set(scriptName) {
            myScriptName.setValue(this, scriptName)
        }
}