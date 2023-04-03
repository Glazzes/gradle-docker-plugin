package com.github.glaze

import com.github.glaze.extensions.BuildImageExtension
import com.github.glaze.extensions.CreateContainerExtension
import com.github.glaze.extensions.DockerExtension
import com.github.glaze.extensions.PushImageExtension
import com.github.glaze.tasks.BuildImageTask
import com.github.glaze.tasks.CreateContainerTask
import com.github.glaze.tasks.PushImageTask
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.plugins.ExtensionAware

class DockerPlugin: Plugin<Project> {
    companion object {
        const val name = "com.github.glazzes.docker-plugin"
    }

    override fun apply(project: Project) {
        val dockerExtension = project.extensions
            .create(DockerExtension.name, DockerExtension::class.java) as ExtensionAware

        dockerExtension.extensions
            .create(BuildImageExtension.name, BuildImageExtension::class.java)
        dockerExtension.extensions
            .create(CreateContainerExtension.name, CreateContainerExtension::class.java)

        dockerExtension.extensions
            .create(PushImageExtension.name, PushImageExtension::class.java)

        project.tasks.register(BuildImageTask.name, BuildImageTask::class.java)
        project.tasks.register(CreateContainerTask.name, CreateContainerTask::class.java)
        project.tasks.register(PushImageTask.name, PushImageTask::class.java)
    }
}
