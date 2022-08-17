package ge.freeuni.jack.project.workspace

import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path

interface JackWorkspace {

        val manifestPath: Path

        val contentRoot: Path get() = manifestPath.parent

        val workspaceRoot: VirtualFile?

        val packages: Collection<Package>

}