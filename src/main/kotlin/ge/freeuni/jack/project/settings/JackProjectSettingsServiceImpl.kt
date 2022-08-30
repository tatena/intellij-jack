package ge.freeuni.jack.project.settings

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.configurationStore.serializeObjectInto
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.util.xmlb.XmlSerializer
import ge.freeuni.jack.toolchain.JackToolchain
import ge.freeuni.jack.toolchain.JackToolchainBase
import org.jdom.Element

private const val serviceName: String = "JackProjectSettings"

@com.intellij.openapi.components.State(
    name = serviceName, storages = [
        Storage(StoragePathMacros.WORKSPACE_FILE),
        Storage("misc.xml", deprecated = true)
    ]
)
class JackProjectSettingsServiceImpl(
    private val project: Project
) : PersistentStateComponent<Element>, JackProjectSettingsService {
    @Volatile
    private var _state: JackProjectSettingsService.State = JackProjectSettingsService.State()

    override var settingsState: JackProjectSettingsService.State
        get() = _state.copy()
        set(newState) {
            if (_state != newState) {
                _state = newState.copy()
                notifySettingsChanged()
            }
        }

    override val toolchain: JackToolchainBase? get() = _state.toolchain

    @Suppress("OverridingDeprecatedMember", "DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun getToolchain(): JackToolchain? = _state.toolchain?.let(JackToolchain::from)


    override fun getState(): Element {
        val element = Element(serviceName)
        serializeObjectInto(_state, element)
        return element
    }

    override fun loadState(element: Element) {
        val rawState = element.clone()
        XmlSerializer.deserializeInto(_state, rawState)
    }

    override fun modify(action: (JackProjectSettingsService.State) -> Unit) {
        settingsState = settingsState.also(action)
    }


    private fun notifySettingsChanged(
    ) {
        PsiManager.getInstance(project).dropPsiCaches()
        DaemonCodeAnalyzer.getInstance(project).restart()
    }
}