package ge.freeuni.jack.language.annotator

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity.ERROR
import com.intellij.lang.annotation.HighlightSeverity.INFORMATION
import com.intellij.psi.PsiElement
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.highlighter.JackSyntaxHighlighter
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.util.JackElementFactory
import ge.freeuni.jack.language.psi.JackReferenceType

class JackAnnotator: Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is JackReferenceType) {
            val jclass = JackUtil.findClass(element.project, element.identifier.text)
            val range = element.textRange
            
            if (jclass != null) {
                holder.newSilentAnnotation(INFORMATION)
                    .range(range)
                    .textAttributes(JackSyntaxHighlighter.ANNOTATION)
                    .create()
            } else {
//                holder.newAnnotation(ERROR, "Unresolved Class reference")
//                    .range(range).highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
//                    .create()
            }
        }
    }

}