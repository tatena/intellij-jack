package ge.freeuni.jack.ide.newProject

import com.intellij.openapi.util.NlsContexts
import ge.freeuni.jack.language.JackIcons
import javax.swing.Icon


sealed class JackProjectTemplate(@Suppress("UnstableApiUsage") @NlsContexts.ListItem val name: String, val icon: Icon)

sealed class JackGenericTemplate(@Suppress("UnstableApiUsage") @NlsContexts.ListItem name: String) :
    JackProjectTemplate(name, JackIcons.FILE) {
    object JackProject : JackGenericTemplate("Jack project")
}

