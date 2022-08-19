package ge.freeuni.jack.editor

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.JackProperty
import ge.freeuni.jack.language.psi.JackReferenceType

class JackLineMarkerProvider: RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (element is JackReferenceType && element.parent is JackProperty) {
            val refName = element.identifier.text
            val optClass = JackUtil.findClass(element.project, refName)
            optClass?.let { jclass ->
                val builder = NavigationGutterIconBuilder.create(JackIcons.FILE)
                    .setTargets(jclass)
                    .setTooltipText("Navigate to class declaration")
                result.add(builder.createLineMarkerInfo(element.firstChild))
            }
        }
    }
}