package ge.freeuni.jack.language.lib

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import ge.freeuni.jack.project.settings.jackSettings
import java.nio.file.Path
import kotlin.io.path.notExists

class JackLibraryProvider: AdditionalLibraryRootsProvider() {
    
    companion object {
        private val libFiles = listOf("Array", "Screen", "Output", "Keyboard", "Math", "Memory", "String", "Sys")
    }

    override fun getAdditionalProjectLibraries(project: Project): MutableCollection<SyntheticLibrary> {

        val path = project.jackSettings.toolchain?.location?.resolve("OS") ?: return mutableListOf()
        
        for (classname in libFiles) {
            javaClass.addStdUnitStubToDirectory(path, classname)
        }
        val file: VirtualFile = VfsUtil.findFile(path, false) ?: return mutableListOf()
        
        return mutableListOf(SyntheticLibrary.newImmutableLibrary(listOf(file)))
    }
}
private fun Class<*>.addStdUnitStubToDirectory(stdlibRoot: Path, classname: String) {
    if (stdlibRoot.notExists()) return

    val stdUnitPath = stdlibRoot.resolve("$classname.jack")
    if (stdUnitPath.notExists()) {
        val stdUnitText = getResource("/lib/$classname.jack")?.readText()
        if (stdUnitText != null) {
            stdUnitPath.toFile().writeText(stdUnitText)
        }
    }
}

