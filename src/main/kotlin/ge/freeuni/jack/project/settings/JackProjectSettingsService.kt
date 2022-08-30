package ge.freeuni.jack.project.settings

import com.intellij.ide.plugins.PluginManagerCore.isUnitTestMode
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Transient
import ge.freeuni.jack.systemIndependentPath
import ge.freeuni.jack.toolchain.JackToolChainProvider
import ge.freeuni.jack.toolchain.JackToolchain
import ge.freeuni.jack.toolchain.JackToolchainBase
import java.nio.file.Paths

interface JackProjectSettingsService {
    data class State(
        var version: Int? = null,
        var toolchainHomeDirectory: String? = null,

        ) {
        @get:Transient
        @set:Transient
        var toolchain: JackToolchainBase?
            get() = toolchainHomeDirectory?.let { JackToolChainProvider.getToolchain(Paths.get(it)) }
            set(value) {
                toolchainHomeDirectory = value?.location?.systemIndependentPath
            }
    }

    val toolchain: JackToolchainBase?

    fun getToolchain(): JackToolchain?


    fun modify(action: (State) -> Unit)
    var settingsState: State


}

val Project.jackSettings: JackProjectSettingsService
    get() = getService(JackProjectSettingsService::class.java)
        ?: error("Failed to get JackProjectSettingsService for $this")

val Project.toolchain: JackToolchainBase?
    get() {
        val toolchain = jackSettings.toolchain
        return when {
            toolchain != null -> toolchain
            isUnitTestMode -> JackToolchainBase.suggest()
            else -> null
        }
    }
