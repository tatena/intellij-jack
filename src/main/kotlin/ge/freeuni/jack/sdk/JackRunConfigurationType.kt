package ge.freeuni.jack.sdk

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import ge.freeuni.jack.language.JackIcons
import javax.swing.Icon


class JackRunConfigurationType : ConfigurationType {
    override fun getDisplayName(): String {
        return "Jack"
    }

    override fun getConfigurationTypeDescription(): String {
        return "Jack run configuration type"
    }

    override fun getIcon(): Icon {
        return JackIcons.FILE
    }

    override fun getId(): String {
        return ID
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(JackConfigurationFactory(this))
    }

    companion object {
        const val ID = "JackRunConfiguration"
    }
}