package com.github.glaze

import com.github.glaze.extensions.DockerExtension
import com.github.glaze.tasks.BuildImageTask
import com.github.glaze.tasks.CreateContainerTask
import com.github.glaze.tasks.PushImageTask
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class DockerPluginTest {
    private val project = ProjectBuilder.builder().build()

    @Test
    fun `plugin registers extensions`() {
        project.plugins.apply(DockerPlugin.name)

        assertNotNull(project.extensions.findByName(DockerExtension.name))
        // assertNotNull(project.extensions.findByName("image"))
        //assertNotNull(project.extensions.findByName("container"))
    }

    @Test
    fun `plugin registers task`() {
        project.plugins.apply(DockerPlugin.name)
        assertNotNull(project.tasks.findByName(CreateContainerTask.name))
        assertNotNull(project.tasks.findByName(BuildImageTask.name))
        assertNotNull(project.tasks.findByName(PushImageTask.name))
    }
}
