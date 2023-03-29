package com.github.glaze

import com.github.glaze.extensions.BuildImageExtension
import com.github.glaze.extensions.CreateContainerExtension
import com.github.glaze.extensions.DockerExtension
import com.github.glaze.tasks.BuildImageTask
import com.github.glaze.tasks.CreateContainerTask
import com.github.glaze.utils.Properties.*
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.plugins.ExtensionAware

class DockerPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val dockerExtension = project.extensions
            .create(ROOT_EXTENSION.value, DockerExtension::class.java) as ExtensionAware

        dockerExtension.extensions
            .create(BUILD_IMAGE_EXTENSION.value, BuildImageExtension::class.java)
        dockerExtension.extensions
            .create(CREATE_CONTAINER_EXTENSION.value, CreateContainerExtension::class.java)

        project.tasks.register(BUILD_IMAGE_TASK.value, BuildImageTask::class.java)
        project.tasks.register(CREATE_CONTAINER_TASK.value, CreateContainerTask::class.java)
    }
}
