package ge.freeuni.jack.language.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache

abstract class JackReferenceBase(@JvmField val elem: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(elem, textRange), PsiPolyVariantReference {
    protected abstract fun resolveInner(incompleteCode: Boolean): List<PsiElement>

    override fun resolve(): PsiElement? {
        val res = multiResolve(false)
        if (res.isNotEmpty()) {
            return res[0].element
        }
        return null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        return ResolveCache.getInstance(elem.project).resolveWithCaching(
            this, { referenceBase, _ ->
                referenceBase.resolveInner(false)
                    .map { PsiElementResolveResult(it) }
                    .toTypedArray()
            }, true, false
        )
    }

    override fun getVariants(): Array<Any> {
//        val classes = JackUtil.findClasses(elem.project)
//        val res = arrayListOf<LookupElement>()
//        for (jclass in classes) {
//            if (jclass.identifier.text.isNotEmpty()) {
//                res.add(LookupElementBuilder.create(jclass)
//                    .withIcon(JackIcons.FILE)
//                    .withTypeText(jclass.containingFile.name))
//            }
//        }
//        return res.toArray()
        return resolveInner(true).toTypedArray()
    }

    override fun hashCode(): Int {
        return elem.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return elem == other
    }
}