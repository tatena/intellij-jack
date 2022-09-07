package ge.freeuni.jack.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.JackAccessExpr
import ge.freeuni.jack.language.psi.JackFunc
import ge.freeuni.jack.language.psi.JackTypes
import ge.freeuni.jack.language.psi.JackVarReference

class JackMethodCompletionProvider : CompletionProvider<CompletionParameters>() {
    
    private fun getFuncType(func: JackFunc): String {
        if (func.primitiveRetType != null) {
            return func.primitiveRetType!!.text
        } 
        if (func.referenceType != null) {
            return func.referenceType!!.identifier.text
        }
        return "UnidentifiedType"
    }
    
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val elem = parameters.position
        val access = elem.parent.parent as? JackAccessExpr ?: return
        val ref = access.accessReference.varReference
        val optClass = JackUtil.findClass(elem.project, ref.identifier.text)
        optClass?.let { jclass ->
            jclass.staticMethods.forEach { func ->
                var paramText = "("
                func.funcParams?.paramList?.forEach { param ->
                    paramText += "${param.type} ${param.propertyDefinition?.identifier?.text}, "
                }
                if (paramText.length > 1) {
                    paramText = paramText.dropLast(2)
                }
                paramText += ")"
                func.funcNameDefinition?.let { name ->
                    result.addElement(
                        LookupElementBuilder.create(name)
                            .withCaseSensitivity(false)
                            .withIcon(JackIcons.FUNC)
                            .withTypeText(getFuncType(func))
                            .withTailText(paramText)
                    )
                }
            }
            return
        }
//        val names: Set<PropertyItem> = JackUtil.getAllVariables(elem.parent as JackVarReference)
//        names.forEach { item ->
//            result.addElement(
//                LookupElementBuilder
//                    .create(item.name)
//                    .withCaseSensitivity(false)
//                    .withTypeText(item.type)
//                    .withIcon(JackIcons.FIELD)
//            )
//        }
            result.addElement(
                LookupElementBuilder
                    .create("BAROOOO")
            )
    }

}