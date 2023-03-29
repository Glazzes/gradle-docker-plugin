package com.github.glaze.tasks

import com.github.glaze.common.Client
import com.github.glaze.extensions.BuildImageExtension
import com.github.glaze.utils.Properties.*
import groovy.lang.MissingPropertyException
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.TaskAction

open class BuildImageTask : DefaultTask() {

    init {
        group = "docker"
        description = "Creates a docker image from a local Dockerfile or remote git repository"
    }

    @TaskAction
    fun buildImage() {
        val dockerExtension = project.extensions
            .getByName(ROOT_EXTENSION.value) as ExtensionAware
        val buildImageExtension = dockerExtension.extensions.getByName(BUILD_IMAGE_EXTENSION.value)
            as BuildImageExtension

        val remote = buildImageExtension.remote.orNull
        if(remote !== null) {
            return
        }

        val dockerFile = buildImageExtension.dockerFile.orNull ?:
            throw MissingPropertyException("Dockerfile is required")

        val buildContext = buildImageExtension.buildContext.orNull ?:
            throw MissingPropertyException("BuildContext is required")

        val tags = buildImageExtension.tags.orNull ?:
            throw MissingPropertyException("Tags is required")

        val pull = buildImageExtension.pull.orNull ?: false
        logger.quiet("All required build Image properties found")

        val dockerClient = Client.getInstance()
        logger.quiet("Creating docker image with tags $tags")

        val buildImageCmd = dockerClient.buildImageCmd()
            .withBaseDirectory(buildContext.asFile)
            .withDockerfile(dockerFile.asFile)
            .withPull(pull)
            .withTags(tags)

        val result = buildImageCmd.start()
        val imageId = result.awaitImageId()
        logger.quiet("Created docker image successfully with id $imageId")
    }

}
