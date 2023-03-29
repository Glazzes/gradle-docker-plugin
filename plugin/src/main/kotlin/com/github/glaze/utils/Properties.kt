package com.github.glaze.utils

enum class Properties(val value: String) {
    ROOT_EXTENSION("docker"),
    PLUGIN_NAME("glaze.docker-plugin"),
    BUILD_IMAGE_TASK("buildImage"),
    BUILD_IMAGE_EXTENSION("image"),
    CREATE_CONTAINER_TASK("createContainer"),
    CREATE_CONTAINER_EXTENSION("container")
}
