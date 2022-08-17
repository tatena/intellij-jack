package ge.freeuni.jack.ide.newProject

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import ge.freeuni.jack.UiDebouncer
import ge.freeuni.jack.ide.projectSettings.JackProjectSettingsPanel

class JackNewProjectPanel(
    private val updateListener: (() -> Unit)? = null
) : Disposable {

    private val jackProjectSettings = JackProjectSettingsPanel(updateListener = updateListener)
    val data: ConfigurationData get() = ConfigurationData(jackProjectSettings.data, JackGenericTemplate.JackProject)
    private val updateDebouncer = UiDebouncer(this)

    override fun dispose() {
        Disposer.dispose(jackProjectSettings)
    }

    fun attachTo(panel: Panel) = with(panel) {
        jackProjectSettings.attachTo(this)

//        if (showProjectTypeSelection) {
//
//            separator("Project Template")
//                .topGap(TopGap.MEDIUM)
//
//            row {
//                resizableRow()
//                fullWidthCell(templateToolbar.createPanel())
//                    .verticalAlign(VerticalAlign.FILL)
//            }
//
//        }

        update()
    }



    fun update() {
        updateDebouncer.run(
            onPooledThread = {
//                when (selectedTemplate) {
//                    is RsGenericTemplate -> false
//                    is RsCustomTemplate -> cargo?.checkNeedInstallCargoGenerate() ?: false
//                }
            },
            onUiThread = { needInstall ->
//                downloadCargoGenerateLink.isVisible = needInstall
//                needInstallCargoGenerate = needInstall
                updateListener?.invoke()
            }
        )
    }

}
