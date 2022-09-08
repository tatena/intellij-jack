package ge.freeuni.jack.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.JackClassDeclaration

class JackClassReferenceCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val classes = JackUtil.findClasses(parameters.originalFile.project)
        for (jclass in classes) {
            val name = jclass.name ?: continue
            val elem = LookupElementBuilder
                .create(name)
                .withCaseSensitivity(true)
                .withTypeText("class")
                .withIcon(JackIcons.FILE)
            result.addElement(elem)
        }
    }
}
