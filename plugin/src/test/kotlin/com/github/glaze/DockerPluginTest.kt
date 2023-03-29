package com.github.glaze

import com.github.glaze.utils.Properties.*
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class DockerPluginTest {
    private val project = ProjectBuilder.builder().build()

    @Test
    fun `plugin registers extensions`() {
        project.plugins.apply(PLUGIN_NAME.value)

        assertNotNull(project.extensions.findByName(ROOT_EXTENSION.value))
        // assertNotNull(project.extensions.findByName("image"))
        //assertNotNull(project.extensions.findByName("container"))
    }

    @Test
    fun `plugin registers task`() {
        project.plugins.apply(PLUGIN_NAME.value)

        assertNotNull(project.tasks.findByName(CREATE_CONTAINER_TASK.value))
        assertNotNull(project.tasks.findByName(BUILD_IMAGE_TASK.value))
    }
}
