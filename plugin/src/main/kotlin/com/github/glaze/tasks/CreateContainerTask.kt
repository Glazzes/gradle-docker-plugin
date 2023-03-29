package com.github.glaze.tasks

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports.Binding
import com.github.glaze.common.Client
import com.github.glaze.extensions.CreateContainerExtension
import com.github.glaze.utils.Properties.*
import com.github.glaze.utils.RegexUtils
import groovy.lang.MissingPropertyException
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.TaskAction

open class CreateContainerTask : DefaultTask() {
    init {
        group = "docker"
        description = "Creates and runs a docker container"
    }

    @TaskAction
    fun createContainer() {
        val dockerExtension = project.extensions.getByName(ROOT_EXTENSION.value) as ExtensionAware
        val containerExtension = dockerExtension.extensions
            .getByName(CREATE_CONTAINER_EXTENSION.value) as CreateContainerExtension

        val image = containerExtension.image.orNull ?:
            throw MissingPropertyException("Image is required in order to start a container")

        val dockerClient = Client.getInstance()
        var hostConfig = HostConfig.newHostConfig()
        val createCommand = dockerClient.createContainerCmd(image)

        val containerName = containerExtension.name.orNull
        if(containerName != null) {
            createCommand.withName(containerName)
        }

        logger.quiet("Creating container \"$containerName\"")
        containerExtension.env.orNull?.let { createCommand.withEnv(it) }
        containerExtension.ports.orNull?.let {
            val portMappings = it.map { port -> RegexUtils.getPortsFromMapping(port) }
            val exposedPorts = portMappings.map { mapping -> ExposedPort(mapping.containerPort) }
            val portBinding = portMappings.mapIndexed { index, ports ->
                val binding = Binding("localhost", ports.containerPort.toString())
                PortBinding(binding, exposedPorts[index])
            }

            hostConfig = HostConfig.newHostConfig().withPortBindings(portBinding)
            createCommand.withExposedPorts(exposedPorts).withHostConfig(hostConfig)
        }

        val response = createCommand.withHostConfig(hostConfig).exec()
        logger.quiet("Container created successfully")

        val startContainerOnComplete = containerExtension.runAfterCreation.orNull ?: false
        if(startContainerOnComplete) {
            dockerClient.startContainerCmd(response.id).exec()
            logger.quiet("Container \"${containerName ?: response.id}\" has been started successfully")
        }
    }
}
