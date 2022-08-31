package ge.freeuni.jack.language

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class JackIcons {
    companion object {
        @JvmStatic
        val FILE = IconLoader.getIcon("/icons/jack-logo.svg", Companion::class.java)

        @JvmStatic
        val FUNC = IconLoader.getIcon("/icons/jack-function.svg", Companion::class.java)

        @JvmStatic
        val METHOD = IconLoader.getIcon("/icons/jack-method.svg", Companion::class.java)

        @JvmStatic
        val FIELD = IconLoader.getIcon("/icons/jack-field.svg", Companion::class.java)

        @JvmStatic
        val STATIC = IconLoader.getIcon("/icons/jack-static.svg", Companion::class.java)
        
        fun resolveProperty(scope: String): Icon {
            return if (scope == "method" || scope == "field") FIELD else STATIC 
        }
    }
}