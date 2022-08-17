package ge.freeuni.jack.ide.newProject

import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.CustomStepProjectGenerator
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel
import com.intellij.platform.DirectoryProjectGenerator
import com.intellij.platform.DirectoryProjectGeneratorBase
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.util.PathUtil
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.pathAsPath
import ge.freeuni.jack.project.settings.jackSettings
import ge.freeuni.jack.toolchain.wsl.computeWithCancelableProgress
import org.bouncycastle.asn1.iana.IANAObjectIdentifiers.directory
import java.io.File
import javax.swing.Icon

class JackDirectoryProjectGenerator : DirectoryProjectGeneratorBase<ConfigurationData>(),
    CustomStepProjectGenerator<ConfigurationData> {

    private var peer: JackProjectGeneratorPeer? = null

    override fun getName(): String = "Jack"
    override fun getLogo(): Icon = JackIcons.FILE
    override fun createPeer(): ProjectGeneratorPeer<ConfigurationData> = JackProjectGeneratorPeer().also { peer = it }

    override fun validate(baseDirPath: String): ValidationResult {
        val crateName = PathUtil.getFileName(baseDirPath)
        val message = peer?.settings?.template?.validateProjectName(crateName) ?: return ValidationResult.OK
        return ValidationResult(message)
    }

    override fun generateProject(project: Project, baseDir: VirtualFile, data: ConfigurationData, module: Module) {
        val (settings, template) = data
//        val cargo = settings.toolchain?.cargo() ?: return
        val path = baseDir.pathAsPath
        println(path)

        val fileName = "$path/data.txt"

        var file = File(fileName)
        val isNewFileCreated :Boolean = file.createNewFile()

        if(isNewFileCreated){
            println("$fileName is created successfully.")
            println("-----------------------------------------")
        } else{
            println("$fileName already exists.")
        }


//        val name = project.name.replace(' ', '_')
//        val generatedFiles = project.computeWithCancelableProgress("Generating Jacl project...") {
//            cargo.makeProject(project, module, baseDir, name, template).unwrapOrThrow()
//        }
//
//        project.jackSettings.modify {
//            it.toolchain = settings.toolchain
//        }
//
////        project.makeDefaultRunConfiguration(template)
//        project.openFiles(generatedFiles)
    }

    override fun createStep(
        projectGenerator: DirectoryProjectGenerator<ConfigurationData>,
        callback: AbstractNewProjectStep.AbstractCallback<ConfigurationData>
    ): AbstractActionWithPanel = JackProjectSettingsStep(projectGenerator)
}
