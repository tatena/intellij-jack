package ge.freeuni.jack.language.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import ge.freeuni.jack.language.JackFileType
import ge.freeuni.jack.language.psi.impl.JackClassNameDefinitionImpl

object JackElementFactory {
    fun createClassNameDef(project: Project, name: String): JackClassNameDefinition {
        val classDecl = createFile(project, name).firstChild as JackClassDeclaration
//        return classDecl.classNameDefinition ?: JackClassNameDefinitionImpl(classDecl.firstChild.node)
        return classDecl.classNameDefinition!!
    }

    fun createFile(project: Project, text: String): JackFile {
        val filename = "dummy.jack"
        val classDecl = "class $text { }"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(filename, JackFileType.INSTANCE, classDecl)
            as JackFile
    }
    // public static SimpleFile createFile(Project project, String text) {
    //    String name = "dummy.simple";
    //    return (SimpleFile) PsiFileFactory.getInstance(project).
    //        createFileFromText(name, SimpleFileType.INSTANCE, text);
    //  }
}
