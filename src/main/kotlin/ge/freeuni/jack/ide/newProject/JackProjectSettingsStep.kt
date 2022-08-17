package ge.freeuni.jack.ide.newProject

import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.ProjectSettingsStepBase
import com.intellij.platform.DirectoryProjectGenerator

open class JackProjectSettingsStep(generator: DirectoryProjectGenerator<ConfigurationData>)
    : ProjectSettingsStepBase<ConfigurationData>(generator, AbstractNewProjectStep.AbstractCallback())
