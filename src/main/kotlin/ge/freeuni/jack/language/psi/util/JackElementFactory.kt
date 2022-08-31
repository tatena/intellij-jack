package ge.freeuni.jack.language.psi.util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import ge.freeuni.jack.language.JackFileType
import ge.freeuni.jack.language.psi.*
import ge.freeuni.jack.language.stub.impl.JackClassNameDefStub
import ge.freeuni.jack.language.stub.type.JackClassNameDefStubElementType
import ge.freeuni.jack.language.stub.type.JackMethodDefinitionStubElementType
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

    fun createMethods(text: List<String>, project: Project): List<JackFunc> {
        val file = createFileWithMethod(text, project)
        val jclass = PsiTreeUtil.findChildOfType(file, JackClassDeclaration::class.java)
        return jclass?.classBody?.funcList ?: listOf()
    }

    private fun createFileWithMethod(text: List<String>, project: Project): JackFile {
        val filename = "dummy.jack"
//        var classDecl = "class Dummy {\n"
//        text.forEach {
//            println(it)
//            classDecl += it
//        }
//        classDecl += "\n}"
//        
        val classDecl = "class Dummy { ${text.reduce { acc, s -> acc + s }} }"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(filename, JackFileType.INSTANCE, classDecl)
                as JackFile
    }

    fun createFunctionDefinition(project: Project, name: String): JackFuncNameDefinition {
        val classDecl = createFileWithFunction(project, propertyName = name).firstChild
                as JackClassDeclaration
        val optBody = classDecl.classBody

        optBody?.let { body ->
            val prop = body.funcList[0]
            if (prop != null && prop.funcNameDefinition != null) {
                return prop.funcNameDefinition!!
            }
        }
        throw RuntimeException("dummy file with single function couldn't be created")
    }

    private fun createFileWithFunction(project: Project, propertyName: String, classRef: String = "DummyClassRef"): JackFile {
        val filename = "dummy.jack"
        val classDecl = "class Dummy { method $classRef $propertyName() { return; } }"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(filename, JackFileType.INSTANCE, classDecl)
                as JackFile
    }
}
//@JvmStatic
fun factory(name: String): JackStubElementType<*, *> = when(name) {
    "CLASS_NAME_DEFINITION" -> JackClassNameDefStubElementType(name)
    "PROPERTY_DEFINITION" -> JackPropertyDefinitionStubElementType(name)
    "FUNC_NAME_DEFINITION" -> JackMethodDefinitionStubElementType(name)
    else -> throw RuntimeException("Unknown element type: $name")
}

inline fun<T> T?.guard(nullClause: () -> Nothing): T {
    return this ?: nullClause()
}