package com.github.glaze.extensions

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import javax.inject.Inject

open class BuildImageExtension @Inject constructor(objects: ObjectFactory) {
    val dockerFile: RegularFileProperty = objects.fileProperty()
    val buildContext: RegularFileProperty = objects.fileProperty()
    val tags: SetProperty<String> = objects.setProperty(String::class.java)
    val pull: Property<Boolean> = objects.property(Boolean::class.java)
    val remote: Property<String> = objects.property(String::class.java)
}
