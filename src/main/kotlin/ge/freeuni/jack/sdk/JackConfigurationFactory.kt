package ge.freeuni.jack.sdk

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project


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