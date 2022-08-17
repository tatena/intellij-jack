package ge.freeuni.jack.project.model

import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.UserDataHolderEx
import com.intellij.openapi.util.registry.Registry
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.messages.Topic
import ge.freeuni.jack.pathAsPath
import ge.freeuni.jack.project.settings.jackSettings
import ge.freeuni.jack.project.workspace.JackWorkspace
import ge.freeuni.jack.toolchain.JackToolchainBase
import java.nio.file.Path
import java.util.concurrent.CompletableFuture


interface JackProjectsService {
    val project: Project
    val allProjects: Collection<JackProject>
    val hasAtLeastOneValidProject: Boolean
    val initialized: Boolean

    fun findProjectForFile(file: VirtualFile): JackProject?
//    fun findPackageForFile(file: VirtualFile): JackWorkspace.Package?

    fun attachCargoProject(manifest: Path): Boolean
    fun attachCargoProjects(vararg manifests: Path)
    fun detachCargoProject(cargoProject: JackProject)
    fun refreshAllProjects(): CompletableFuture<out List<JackProject>>
    fun discoverAndRefresh(): CompletableFuture<out List<JackProject>>
    fun suggestManifests(): Sequence<VirtualFile>


    companion object {
//        val CARGO_PROJECTS_TOPIC: Topic<CargoProjectsListener> = Topic(
//            "cargo projects changes",
//            CargoProjectsListener::class.java
//        )
//
//        val CARGO_PROJECTS_REFRESH_TOPIC: Topic<CargoProjectsRefreshListener> = Topic(
//            "Cargo refresh",
//            CargoProjectsRefreshListener::class.java
//        )
    }

    fun interface JackProjectsListener {
        fun cargoProjectsUpdated(service: JackProjectsService, projects: Collection<JackProject>)
    }

    interface JackProjectsRefreshListener {
        fun onRefreshStarted()
        fun onRefreshFinished(status: JackRefreshStatus)
    }

    enum class JackRefreshStatus {
        SUCCESS,
        FAILURE,
        CANCEL
    }


    interface Package : UserDataHolderEx {
        val contentRoot: VirtualFile?
        val rootDirectory: Path

        val id: String
        val name: String
        val normName: String get() = name.replace('-', '_')

        val version: String

        val source: String?
//        val origin: PackageOrigin

//        val targets: Collection<Target>
//        val libTarget: Target? get() = targets.find { it.kind.isLib }
//        val customBuildTarget: Target? get() = targets.find { it.kind == TargetKind.CustomBuild }
//        val hasCustomBuildScript: Boolean get() = customBuildTarget != null

//        val dependencies: Collection<Dependency>


//        val cfgOptions: CfgOptions?

//        val features: Set<PackageFeature>

        val workspace: JackWorkspace

//        val edition: Edition

        val env: Map<String, String>

        val outDir: VirtualFile?

//        val featureState: Map<FeatureName, FeatureState>

//        val procMacroArtifact: CargoWorkspaceData.ProcMacroArtifact?

//        fun findDependency(normName: String): Target? =
//            if (this.normName == normName) libTarget else dependencies.find { it.name == normName }?.pkg?.libTarget
//    }

    }

    val Project.jackProjects: JackProjectsService get() = service()

//    fun JackProjectsService.isGeneratedFile(file: VirtualFile): Boolean {
////    val outDir = findPackageForFile(file)?.outDir ?: return false
////    return VfsUtil.isAncestor(outDir, file, false)
//    }

    interface JackProject : UserDataHolderEx {
        val project: Project
        val manifest: Path
        val rootDir: VirtualFile?
        val workspaceRootDir: VirtualFile?

        val presentableName: String
        val workspace: JackWorkspace?

//    val procMacroExpanderPath: Path?

        val workspaceStatus: UpdateStatus
//    val stdlibStatus: UpdateStatus
//    val rustcInfoStatus: UpdateStatus

//    val mergedStatus: UpdateStatus
//        get() = workspaceStatus
//            .merge(stdlibStatus)
//            .merge(rustcInfoStatus)


        sealed class UpdateStatus(private val priority: Int) {
            object UpToDate : UpdateStatus(0)
            object NeedsUpdate : UpdateStatus(1)
            class UpdateFailed(@Suppress("UnstableApiUsage") @NlsContexts.Tooltip val reason: String) : UpdateStatus(2)

            fun merge(status: UpdateStatus): UpdateStatus = if (priority >= status.priority) this else status
        }
    }

    fun guessAndSetupRustProject(project: Project, explicitRequest: Boolean = false): Boolean {
        if (!explicitRequest) {
            val alreadyTried = run {
                val key = "org.rust.cargo.project.model.PROJECT_DISCOVERY"
                val properties = PropertiesComponent.getInstance(project)
                val alreadyTried = properties.getBoolean(key)
                properties.setValue(key, true)
                alreadyTried
            }
            if (alreadyTried) return false
        }

        val toolchain = project.jackSettings.toolchain
//   || !toolchain.looksLikeValidToolchain()
        if (toolchain == null) {
            discoverToolchain(project)
            return true
        }
        if (!project.jackProjects.hasAtLeastOneValidProject) {
            project.jackProjects.discoverAndRefresh()
            return true
        }
        return false
    }

    private fun discoverToolchain(project: Project) {
        val projectPath = project.guessProjectDir()?.pathAsPath
        val toolchain = JackToolchainBase.suggest(projectPath) ?: return
        invokeLater {
            if (project.isDisposed) return@invokeLater

            val oldToolchain = project.jackSettings.toolchain
//         && oldToolchain.looksLikeValidToolchain()
            if (oldToolchain != null) {
                return@invokeLater
            }

            runWriteAction {
                project.jackSettings.modify { it.toolchain = toolchain }
            }

//        project.showBalloon("Using $tool", NotificationType.INFORMATION)
            project.jackProjects.discoverAndRefresh()
        }
    }
}

    fun ContentEntry.setup(contentRoot: VirtualFile) {
        val makeVfsUrl = { dirName: String -> contentRoot.findChild(dirName)?.url }
//    CargoConstants.ProjectLayout.sources.mapNotNull(makeVfsUrl).forEach {
        addSourceFolder("src", false)
        addSourceFolder("examples", false)
//    }
//    CargoConstants.ProjectLayout.tests.mapNotNull(makeVfsUrl).forEach {
//        addSourceFolder(it, /* test = */ true)
//    }

//    makeVfsUrl(CargoConstants.ProjectLayout.target)?.let(::addExcludeFolder)

    }



val Project.jackProjects: JackProjectsService get() = service()
