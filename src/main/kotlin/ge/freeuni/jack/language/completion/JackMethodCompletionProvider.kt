package ge.freeuni.jack.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.*

class JackMethodCompletionProvider : CompletionProvider<CompletionParameters>() {
    
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val elem = parameters.position
        val access = elem.parent.parent as? JackAccessExpr ?: return
        val ref = access.accessReference.varReference
        JackUtil.findClass(elem.project, ref.identifier.text)?.let { 
            handleClass(it, result)
            return
        }
        
        val resolvedElem = ref.resolve()?.parent ?: return
        println("type is: ${resolvedElem.elementType}")
        val resolved = when (resolvedElem.elementType) {
            JackTypes.PROPERTY -> (resolvedElem as JackProperty).type
            JackTypes.PARAM -> (resolvedElem as JackParam).type
            JackTypes.LOCAL_VARS -> (resolvedElem as JackLocalVars).type
            else -> return
        }

        println("resolved class name: $resolved")
        JackUtil.findClass(resolvedElem.project, resolved)?.let { 
            handleClass(it, result, true) 
        }
    }

    private fun handleClass(
        jclass: JackClassDeclaration,
        result: CompletionResultSet,
        members: Boolean = false                    
    ) {
        val methods = if (members) jclass.memberMethods else jclass.staticMethods
        methods.forEach { func ->
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
                    LookupElementBuilder.create(name.identifier.text)
                        .withCaseSensitivity(true)
                        .withIcon(if (members) JackIcons.METHOD else JackIcons.FUNC)
                        .withTypeText(getFuncType(func))
                        .withTailText(paramText)
                        .withInsertHandler { ctx, _ ->
                            ctx.document.insertString(
                                ctx.selectionEndOffset, "()"
                            )
                            EditorModificationUtil.moveCaretRelatively(ctx.editor, 1)
                        }
                )
            }
        }
    }

}
 fun getFuncType(func: JackFunc): String {
    if (func.primitiveRetType != null) {
        return func.primitiveRetType!!.text
    }
    if (func.referenceType != null) {
        return func.referenceType!!.identifier.text
    }
    return "UnidentifiedType"
}
