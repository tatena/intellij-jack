package ge.freeuni.jack.language.psi.impl

import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.JackTypes

object JackPsiImplUtil {
    @JvmStatic
    fun getClassName(property: JackClassDeclaration): String? {
        val node = property.node.findChildByType(JackTypes.CLASS_NAME_DEFINITION)

        val name = node?.findChildByType(JackTypes.IDENTIFIER)?.text
        return name
    }
}