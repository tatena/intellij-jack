package ge.freeuni.jack.project.settings

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.configurationStore.serializeObjectInto
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiManager
import com.intellij.util.ThreeState
import com.intellij.util.xmlb.XmlSerializer
import ge.freeuni.jack.toolchain.JackToolchain
import ge.freeuni.jack.toolchain.JackToolchainBase
import org.jdom.Element
import org.jetbrains.annotations.TestOnly

private const val serviceName: String = "JackProjectSettings"

@com.intellij.openapi.components.State(name = serviceName, storages = [
    Storage(StoragePathMacros.WORKSPACE_FILE),
    Storage("misc.xml", deprecated = true)
])
class JackProjectSettingsServiceImpl(
    private val project: Project
) : PersistentStateComponent<Element>, JackProjectSettingsService {
    @Volatile
    private var _state: JackProjectSettingsService.State = JackProjectSettingsService.State()

    override var settingsState: JackProjectSettingsService.State
        get() = _state.copy()
        set(newState) {
            if (_state != newState) {
                val oldState = _state
                _state = newState.copy()
                notifySettingsChanged(oldState, newState)
            }
        }

    override val toolchain: JackToolchainBase? get() = _state.toolchain
//        override val autoShowErrorsInEditor: ThreeState get() = ThreeState.fromBoolean(_state.autoShowErrorsInEditor)
//        override val autoUpdateEnabled: Boolean get() = _state.autoUpdateEnabled
//        override val explicitPathToStdlib: String? get() = _state.explicitPathToStdlib
//        override val externalLinter: ExternalLinter get() = _state.externalLinter
//        override val runExternalLinterOnTheFly: Boolean get() = _state.runExternalLinterOnTheFly
//        override val externalLinterArguments: String get() = _state.externalLinterArguments
//        override val compileAllTargets: Boolean get() = _state.compileAllTargets
//        override val useOffline: Boolean get() = _state.useOffline
//        override val macroExpansionEngine: MacroExpansionEngine get() = _state.macroExpansionEngine
//        override val doctestInjectionEnabled: Boolean get() = _state.doctestInjectionEnabled

    @Suppress("OverridingDeprecatedMember", "DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun getToolchain(): JackToolchain? = _state.toolchain?.let(JackToolchain::from)


    override fun getState(): Element {
        val element = Element(serviceName)
        serializeObjectInto(_state, element)
        return element
    }

    override fun loadState(element: Element) {
        val rawState = element.clone()
//        rawState.updateToCurrentVersion(
        XmlSerializer.deserializeInto(_state, rawState)

//        if (_state.macroExpansionEngine == MacroExpansionEngine.OLD) {
//            _state.macroExpansionEngine = MacroExpansionEngine.NEW
//        }
    }

    override fun modify(action: (JackProjectSettingsService.State) -> Unit) {
        settingsState = settingsState.also(action)
    }


//
//    override fun configureToolchain() {
//        project.showSettingsDialog<RsProjectConfigurable>()
//    }

    private fun notifySettingsChanged(oldState: JackProjectSettingsService.State, newState: JackProjectSettingsService.State) {
//        val event = RustSettingsChangedEvent(oldState, newState)
//        project.messageBus.syncPublisher(RUST_SETTINGS_TOPIC).rustSettingsChanged(event)
//
//        if (event.isChanged(State::doctestInjectionEnabled)) {
//            // flush injection cache
            PsiManager.getInstance(project).dropPsiCaches()
//        }
//        if (event.affectsHighlighting) {
            DaemonCodeAnalyzer.getInstance(project).restart()
//        }
    }
}