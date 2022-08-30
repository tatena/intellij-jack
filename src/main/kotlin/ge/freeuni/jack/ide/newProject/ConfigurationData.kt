package ge.freeuni.jack.ide.newProject

import ge.freeuni.jack.ide.projectSettings.JackProjectSettingsPanel

data class ConfigurationData(
    val settings: JackProjectSettingsPanel.Data,
    val template: JackProjectTemplate
)