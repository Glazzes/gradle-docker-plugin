## Docker plugin 🐘

### About
This project is a simple gradle plugin that allows to 
create docker images, create/run containers and push image to a
desired registry from gradle by executing its bundled tasks.

All operations are done over 
[Docker's engine API](https://docs.docker.com/engine/api/v1.42/)
through [docker-java library](https://github.com/docker-java/docker-java).

### Project goals
I've been reading gradle documentation for a week at this point along
with [Gradle in action](https://www.google.com/search?q=gradle+in+action),
I wanted to learn more about how Gradle works and what it has to offer,
this is a simple hobby project as a way to get a deeper understanding of
how to get the most value of this awesome technology.

### How to build 
As this project makes use of [version catalogs](https://docs.gradle.org/current/userguide/platforms.html#sec:sharing-catalogs), you should
use the gradle wrapper, with that said run:
```bash
./gradlew plugin:publishToMavenLocal
```

### How to use
As you have uploaded your plugin to your local 
repository, gradle needs to know where to find your
plugins
```kotlin
// Add to settings.gradle file
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

// Add to your build.gradle file
plugins {
    // plugins here
    id("com.github.glazzes.docker-plugin") version "0.0.1"
    // other plugins here
}
```

### Using tasks
This plugin includes three tasks `buildImage`, `createContainer` and 
`pushImage` that can be configured as follows:

#### Build image task
`buildImage` creates a docker image from a `Dockerfile`, this one can be
configured with the following properties:

| Name           | Description                                             | Required | Type          |
|----------------|---------------------------------------------------------|----------|---------------|
| `dockerfile`   | Dockerfile to build your image                          | Yes      | java.io.File  |
| `buildContext` | Directory where your Dockerfile lives and/or uses files | Yes      | java.io.File  |
| `tags`         | Tags for your image                                     | No       | List\<String> |
| `pull`         | Pull image if not present                               | No       | boolean       |
| `remote`       | Url to git repository                                   | No       | String        |

Example:
```groovy
docker {
    // for a local image build using the following config
    image {
        dockerfile.set(project.file("Dockerfile"))
        buildContext.set(project.projectDir)
        tags.addAll(listOf("myImage:0.0.1"))
        // pull.set(true)
    }
    
    // for a git repository use
    image {
        remote.set("url-to-your-repo")
        // pull.set(true)
    }
}
```

#### Create container task
`createContainer` creates a docker container and runs it afterwards, this one
can be configured with the following properties:

| Name               | Description                                       | Required | Type          |
|--------------------|---------------------------------------------------|----------|---------------|
| `image`            | Docker image used by your container               | Yes      | String        |
| `name`             | Name used by your new container                   | No       | String        |
| `ports`            | Port bindings, for instance `3000:80`             | No       | List\<String> |
| `env`              | Environment variables, for instance `API_KEY=key` | No       | List\<String> |
| `runAfterCreation` | Start container once created, default `false`     | No       | boolean       |

Example:
```groovy
docker {
    container {
        image.set("redis:7.0-alpine")
        name.set("my-redis")
        ports.addAll(listOf("6379:6379"))
        env.addAll(listOf("RANDOM_KEY=myrandomkey", "SOMETHING=value"))
        runAfterCreation.set(true)
    }
}
```

#### Push image task
`pushImage` pushes a local image to a container registry of your choice,
this one can be configured with the following properties:

| Name            | Description                                                                            | Required | Type   |
|-----------------|----------------------------------------------------------------------------------------|----------|--------|
| `image`         | Image name matching registry name i.e `username/image`                                 | Yes      | String |
| `tag`           | Tag i.e `latest`                                                                       | Yes      | String |
| `registry`      | url to your registry image repository i.e `registry.hub.docker.com/glazee/testing`     | Yes      | String |
| `username`      | registry account username                                                              | No       | String |
| `password`      | registry account password                                                              | No       | String |
| `email`         | registry account email                                                                 | No       | String |
| `identityToken` | see [authentication](https://docs.docker.com/engine/api/v1.42/#section/Authentication) | No       | String |
Examples:
```groovy
docker {
    
    // with authentication config
    push {
        image.set("username/imageName")
        tag.set("0.0.1")
        registry.set("registry.hub.docker.com/username/imageName")
        username.set("username")
        password.set("password")
        email.set("username@gmail.com")
    }
    
    // with identity token
    push {
        image.set("username/imageName")
        tag.set("0.0.1")
        registry.set("registry.hub.docker.com/username/imageName")
        identityToken.set("token")
    }
}
```
