package ge.freeuni.jack.language.psi.util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.tree.IElementType
import ge.freeuni.jack.language.JackFileType
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.JackFile
import ge.freeuni.jack.language.psi.JackPropertyDefinition
import ge.freeuni.jack.language.psi.JackReferenceType
import ge.freeuni.jack.language.stub.impl.JackClassNameDefStub
import ge.freeuni.jack.language.stub.type.JackClassNameDefStubElementType
import ge.freeuni.jack.language.stub.type.JackPropertyDefinitionStubElementType
import ge.freeuni.jack.language.stub.type.JackStubElementType

object JackElementFactory {
    fun createClassNameDef(project: Project, name: String): JackClassNameDefinition {
        val classDecl = createFile(project, name).firstChild as JackClassDeclaration
//        return classDecl.classNameDefinition ?: JackClassNameDefinitionImpl(classDecl.firstChild.node)
        return classDecl.classNameDefinition!!
    }

    private fun createFile(project: Project, className: String): JackFile {
        val filename = "dummy.jack"
        val classDecl = "class $className { }"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(filename, JackFileType.INSTANCE, classDecl)
            as JackFile
    }
    
    private fun createFileWithProperty(project: Project, classRef: String = "DummyClassRef", propertyName: String = "dummyVar"): JackFile {
        val filename = "dummy.jack"
        val classDecl = "class Dummy { field $classRef $propertyName; }"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(filename, JackFileType.INSTANCE, classDecl)
                as JackFile
    }

    fun createPropertyFromText(project: Project, name: String): JackReferenceType {
        val classDecl = createFileWithProperty(project, classRef = name).firstChild
                as JackClassDeclaration
        val body = classDecl.classBody
        if (body != null) {
            val ref = body.propertyList[0].referenceType
            if (ref != null) {
                return ref
            }
        }
        throw RuntimeException("dummy file with single property couldn't be created")
    }
    // public static SimpleFile createFile(Project project, String text) {
    //    String name = "dummy.simple";
    //    return (SimpleFile) PsiFileFactory.getInstance(project).
    //        createFileFromText(name, SimpleFileType.INSTANCE, text);
    //  }
    

    fun createPropertyDefinition(project: Project, name: String): JackPropertyDefinition {
        val classDecl = createFileWithProperty(project, propertyName = name).firstChild
                as JackClassDeclaration
        val optBody = classDecl.classBody
        
        optBody?.let { body -> 
            val prop = body.propertyList[0].propertyDefinitionList[0]
            if (prop != null) {
                return prop
            }
        }
        throw RuntimeException("dummy file with single property couldn't be created")
    }
}
//@JvmStatic
fun factory(name: String): JackStubElementType<*, *> = when(name) {
    "CLASS_NAME_DEFINITION" -> JackClassNameDefStubElementType(name)
    "PROPERTY_DEFINITION" -> JackPropertyDefinitionStubElementType(name)
    else -> throw RuntimeException("Unknown element type: $name")
}
