package ge.freeuni.jack.ide.newProject


import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import ge.freeuni.jack.language.JackIcons
import javax.swing.Icon

class JackModuleType : ModuleType<JackModuleBuilder>(ID) {
    override fun getNodeIcon(isOpened: Boolean): Icon = JackIcons.FILE

    override fun createModuleBuilder(): JackModuleBuilder = JackModuleBuilder()

    override fun getDescription(): String = "Jack module"

    override fun getName(): String = "Jack"

    companion object {
        const val ID = "JACK_MODULE"
        val INSTANCE: JackModuleType by lazy { ModuleTypeManager.getInstance().findByID(ID) as JackModuleType }
    }

}