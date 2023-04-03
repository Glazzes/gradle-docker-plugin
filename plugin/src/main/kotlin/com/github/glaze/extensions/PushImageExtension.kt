package com.github.glaze.extensions

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class PushImageExtension @Inject constructor(objects: ObjectFactory) {
    val image: Property<String> = objects.property(String::class.java)

    val username: Property<String> = objects.property(String::class.java)
    val password: Property<String> = objects.property(String::class.java)
    val email: Property<String> = objects.property(String::class.java)
    val identityToken: Property<String> = objects.property(String::class.java)

    val registry: Property<String> = objects.property(String::class.java)
        .convention("registry.hub.docker.com")

    val tag: Property<String> = objects.property(String::class.java)
        .convention("latest")

    companion object {
        const val name = "push"
    }
}
