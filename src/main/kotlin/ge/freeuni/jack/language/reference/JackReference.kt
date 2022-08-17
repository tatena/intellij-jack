package ge.freeuni.jack.language.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil

class JackReference(@JvmField val elem: PsiElement, textRange: TextRange): 
    PsiReferenceBase<PsiElement>(elem, textRange), PsiPolyVariantReference
{
    private val name: String
    
    init {
        name = elem.text.substring(textRange.startOffset, textRange.endOffset)
    }
    override fun resolve(): PsiElement? {
        return multiResolve(false)[0].element
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val optClass = JackUtil.findClass(elem.project, name)
        optClass?.let { jclass ->
            return arrayOf(PsiElementResolveResult(jclass))
        }
        return arrayOf()
    }

    override fun getVariants(): Array<Any> {
        val classes = JackUtil.findClasses(elem.project)
        val res = arrayListOf<LookupElement>()
        for (jclass in classes) {
            if (jclass.identifier.text.isNotEmpty()) {
                res.add(LookupElementBuilder.create(jclass)
                    .withIcon(JackIcons.FILE)
                    .withTypeText(jclass.containingFile.name))
            }
        }
        return res.toArray()
    }
    

//  @Override
//  public Object @NotNull [] getVariants() {
//    Project project = myElement.getProject();
//    List<SimpleProperty> properties = SimpleUtil.findProperties(project);
//    List<LookupElement> variants = new ArrayList<>();
//    for (final SimpleProperty property : properties) {
//      if (property.getKey() != null && property.getKey().length() > 0) {
//        variants.add(LookupElementBuilder
//                .create(property).withIcon(SimpleIcons.FILE)
//                .withTypeText(property.getContainingFile().getName())
//        );
//      }
//    }
//    return variants.toArray();
//  }
//
//}

}