package ge.freeuni.jack.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import ge.freeuni.jack.language.JackIcons

class JackCreateFileAction: CreateFileFromTemplateAction(ACTION_NAME, "Creates new class", JackIcons.FILE) {
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("New $ACTION_NAME").addKind("Class", JackIcons.FILE, ACTION_NAME)
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = ACTION_NAME
    
    companion object {
        private const val ACTION_NAME="Jack Class"
    }
}