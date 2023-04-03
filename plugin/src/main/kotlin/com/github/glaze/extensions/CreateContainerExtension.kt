package com.github.glaze.extensions

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

open class CreateContainerExtension @Inject constructor(objects: ObjectFactory) {
    val image: Property<String> = objects.property(String::class.java)
    val name: Property<String> = objects.property(String::class.java)
    val runAfterCreation: Property<Boolean> = objects.property(Boolean::class.java)
    val ports: ListProperty<String> = objects.listProperty(String::class.java)
    val env: ListProperty<String> = objects.listProperty(String::class.java)

    companion object {
        const val name = "container"
    }
}
