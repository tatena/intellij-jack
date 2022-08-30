@file:Suppress("UnstableApiUsage")

package ge.freeuni.jack.ide.newProject

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.ui.dsl.builder.Panel
import ge.freeuni.jack.UiDebouncer
import ge.freeuni.jack.ide.projectSettings.JackProjectSettingsPanel

class JackNewProjectPanel(
    private val updateListener: (() -> Unit)? = null
) : Disposable {

    private val jackProjectSettings = JackProjectSettingsPanel()
    val data: ConfigurationData get() = ConfigurationData(jackProjectSettings.data, JackGenericTemplate.JackProject)
    private val updateDebouncer = UiDebouncer(this)

    override fun dispose() {
        Disposer.dispose(jackProjectSettings)
    }

    fun attachTo(panel: Panel) = with(panel) {
        jackProjectSettings.attachTo(this)
        update()
    }


    private fun update() {
        updateDebouncer.run(
            onPooledThread = {
                false
            },
            onUiThread = { _ ->
                updateListener?.invoke()
            }
        )
    }

}
