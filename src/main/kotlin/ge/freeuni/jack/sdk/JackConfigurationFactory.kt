package ge.freeuni.jack.sdk

import com.intellij.execution.configurations.*
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project
import com.intellij.util.PlatformUtils
import ge.freeuni.jack.language.JackIcons


class JackConfigurationFactory  constructor(type: ConfigurationType?) : ConfigurationFactory(type!!) {
    override fun getId(): String {
        return JackRunConfigurationType.ID
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return JackRunConfiguration(project, this, "Jack")
    }

    override fun getOptionsClass(): Class<out BaseState?> {
        return JackRunConfigurationOptions::class.java
    }
}

class JackCommandConfigurationType : ConfigurationTypeBase(
    "JackCommandRunConfiguration",
    "Jack",
    "Jack command run configuration",
    JackIcons.FILE
) {
    init {
        addFactory(JackConfigurationFactory(this))
    }

    val factory: ConfigurationFactory get() = configurationFactories.single()

    companion object {
        fun getInstance(): JackCommandConfigurationType =
            ConfigurationTypeUtil.findConfigurationType(JackCommandConfigurationType::class.java)
    }
}