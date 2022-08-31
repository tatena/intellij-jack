@file:Suppress("UnstableApiUsage")

package ge.freeuni.jack.ide.projectSettings

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.intellij.ui.dsl.builder.Panel
import ge.freeuni.jack.UiDebouncer
import ge.freeuni.jack.fullWidthCell
import ge.freeuni.jack.project.JackToolchainPathChoosingComboBox
import ge.freeuni.jack.project.settings.JackProjectSettingsService
import ge.freeuni.jack.toolchain.JackToolChainProvider
import ge.freeuni.jack.toolchain.JackToolchainBase
import ge.freeuni.jack.toolchain.flavors.JackToolChainFlavor


class JackProjectSettingsPanel : Disposable {

    data class Data(
        val toolchain: JackToolchainBase?,
    )


    override fun dispose() {
        Disposer.dispose(pathToToolchainComboBox)
    }

    private val pathToToolchainComboBox = JackToolchainPathChoosingComboBox { update() }

    private val versionUpdateDebouncer = UiDebouncer(this)

    var data: Data
        get() {
            val toolchain = pathToToolchainComboBox.selectedPath?.let { JackToolChainProvider.getToolchain(it) }
            return Data(
                toolchain = toolchain,
            )
        }
        set(value) {
            pathToToolchainComboBox.selectedPath = value.toolchain?.location
            update()
        }

    fun attachTo(panel: Panel) = with(panel) {
        data = Data(
            toolchain = ProjectManager.getInstance().defaultProject
                .getService(JackProjectSettingsService::class.java)
                ?.toolchain
        )

        row("Jack Tools Location") {
            fullWidthCell(pathToToolchainComboBox)
        }

        pathToToolchainComboBox.addToolchainsAsync {
            JackToolChainFlavor.getApplicableFlavors().flatMap { it.suggestHomePaths() }.distinct()
        }
    }

    private fun update() {
        val pathToToolchain = pathToToolchainComboBox.selectedPath
        versionUpdateDebouncer.run(
            onPooledThread = {
                pathToToolchain?.let { JackToolChainProvider.getToolchain(it) }
            },
            onUiThread = {
            }
        )
    }

    @Throws(ConfigurationException::class)
    fun validateSettings() {
        val toolchain = data.toolchain ?: return
        if (!toolchain.looksLikeValidToolchain()) {
            throw ConfigurationException("Invalid tools path")
        }
    }
}