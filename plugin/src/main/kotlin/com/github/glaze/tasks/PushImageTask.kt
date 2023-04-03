package com.github.glaze.tasks

import com.github.dockerjava.api.model.AuthConfig
import com.github.glaze.common.Client
import com.github.glaze.extensions.DockerExtension
import com.github.glaze.extensions.PushImageExtension
import groovy.lang.MissingPropertyException
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.TaskAction

open class PushImageTask : DefaultTask() {

    companion object {
        const val name = "pushImage"
    }

    init {
        group = "docker"
        description = "Pushes an image to a given registry"
    }

    @TaskAction
    fun publishImage() {
        val dockerExtension = project.extensions
            .getByName(DockerExtension.name) as ExtensionAware
        val pushExtension = dockerExtension.extensions.getByType(PushImageExtension::class.java)

        val tag = pushExtension.tag.get()
        val imageName = pushExtension.image.orNull ?:
            throw MissingPropertyException("Image name is required")

        val dockerClient = Client.getInstance()
        val pushCommand = dockerClient.pushImageCmd(imageName)
            .withTag(tag)

        val identityToken = pushExtension.identityToken.orNull
        if(identityToken != null) {
            logger.quiet("Pushing image with identity token $identityToken")
            val config = AuthConfig()
                .withIdentityToken(identityToken)
            config.withIdentityToken(identityToken)

            val callback = pushCommand.start()
            callback.awaitCompletion()
            logger.quiet("Pushed image successfully!")
            return
        }

        logger.quiet("Push image with authentication configuration")
        val username = pushExtension.username.orNull ?:
            throw MissingPropertyException("Username is required")
        val password = pushExtension.password.orNull ?:
            throw MissingPropertyException("Password is required")
        val email = pushExtension.email.orNull ?:
            throw MissingPropertyException("Email is required")
        val registry = pushExtension.registry.get()

        val config = AuthConfig()
            .withUsername(username)
            .withPassword(password)
            .withEmail(email)
            .withRegistryAddress(registry)

        pushCommand.withAuthConfig(config)
        val result = pushCommand.start()
        result.awaitCompletion()
        logger.quiet("Image $imageName has been pushed successfully")
    }

}
