package ge.freeuni.jack.project

import com.intellij.openapi.application.AppUIExecutor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.ComboBoxWithWidePopup
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.ui.AnimatedIcon
import com.intellij.ui.ComboboxSpeedSearch
import com.intellij.ui.components.fields.ExtendableTextComponent
import com.intellij.ui.components.fields.ExtendableTextField
import ge.freeuni.jack.addTextChangeListener
import ge.freeuni.jack.pathAsPath
import ge.freeuni.jack.toPathOrNull
import java.nio.file.Path
import javax.swing.plaf.basic.BasicComboBoxEditor


class JackToolchainPathChoosingComboBox(onTextChanged: () -> Unit = {}) :
    ComponentWithBrowseButton<ComboBoxWithWidePopup<Path>>(
        ComboBoxWithWidePopup(), null
    ) {
    private val editor: BasicComboBoxEditor = object : BasicComboBoxEditor() {
        override fun createEditorComponent(): ExtendableTextField = ExtendableTextField()
    }

    private val pathTextField: ExtendableTextField
        get() = childComponent.editor.editorComponent as ExtendableTextField

    private val busyIconExtension: ExtendableTextComponent.Extension =
        ExtendableTextComponent.Extension { AnimatedIcon.Default.INSTANCE }

    var selectedPath: Path?
        get() = pathTextField.text?.toPathOrNull()
        set(value) {
            pathTextField.text = value?.toString().orEmpty()
        }

    init {
        ComboboxSpeedSearch(childComponent)
        childComponent.editor = editor
        childComponent.isEditable = true

        addActionListener {
            // Select directory with Jack tools
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            FileChooser.chooseFile(descriptor, null, null) { file ->
                childComponent.selectedItem = file.pathAsPath
            }
        }

        pathTextField.addTextChangeListener { onTextChanged() }
    }

    private fun setBusy(busy: Boolean) {
        if (busy) {
            pathTextField.addExtension(busyIconExtension)
        } else {
            pathTextField.removeExtension(busyIconExtension)
        }
        repaint()
    }


    @Suppress("UnstableApiUsage")
    fun addToolchainsAsync(toolchainObtainer: () -> List<Path>, callback: () -> Unit) {
        setBusy(true)
        ApplicationManager.getApplication().executeOnPooledThread {
            var toolchains = emptyList<Path>()
            try {
                toolchains = toolchainObtainer()
            } finally {
                val executor = AppUIExecutor.onUiThread(ModalityState.any()).expireWith(this)
                executor.execute {
                    setBusy(false)
                    val oldSelectedPath = selectedPath
                    childComponent.removeAllItems()
                    toolchains.forEach(childComponent::addItem)
                    selectedPath = oldSelectedPath
                    callback()
                }
            }
        }
    }

    fun addToolchainsAsync(toolchainObtainer: () -> List<Path>) {
        addToolchainsAsync(toolchainObtainer) {}
    }
}
