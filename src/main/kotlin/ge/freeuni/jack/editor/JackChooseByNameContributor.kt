package ge.freeuni.jack.editor

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.ChooseByNameContributorEx2
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter
import ge.freeuni.jack.language.psi.JackNamedElement
import ge.freeuni.jack.language.stub.JackClassNameIndex

class JackChooseByNameContributor: ChooseByNameContributorEx {
    private val indexKeys = listOf(JackClassNameIndex.KEY)

    override fun processNames(processor: Processor<in String>, scope: GlobalSearchScope, filter: IdFilter?) {
        for (key in indexKeys) {
            ProgressManager.checkCanceled()
            StubIndex.getInstance().processAllKeys(
                key, processor, scope, filter
            )
        }
    }

    override fun processElementsWithName(
        name: String,
        processor: Processor<in NavigationItem>,
        parameters: FindSymbolParameters
    ) {
        for (key in indexKeys) {
            ProgressManager.checkCanceled()
            StubIndex.getInstance().processElements(
                key,
                name,
                parameters.project,
                parameters.searchScope,
                parameters.idFilter,
                JackNamedElement::class.java,
                processor
            )
        }
    }
}