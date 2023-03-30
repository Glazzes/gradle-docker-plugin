## Docker plugin 🐘

### About
This project is a simple gradle plugin that allows to 
create docker images and containers from gradle
by executing its bundled tasks.

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
This plugin includes two tasks `buildImage` and `createContainer` that
can be configured as follows:

#### Build image task
This task can be configured using the following properties:

| Name         | Description                                             | Required | Type          |
|--------------|---------------------------------------------------------|----------|---------------|
| dockerfile   | Dockerfile to build your image                          | Yes      | java.io.File  |
| buildContext | Directory where your Dockerfile lives and/or uses files | Yes      | java.io.File  |
| tags         | Tags for your image                                     | No       | List\<String> |
| pull         | Pull image if not present                               | No       | boolean       |
| remote       | Url to git repository                                   | No       | String        |

Here's an example:
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
This task can be configured with the following properties:

| Name             | Description                                       | Required | Type          |
|------------------|---------------------------------------------------|----------|---------------|
| image            | Docker image used by your container               | Yes      | String        |
| name             | Name used by your new container                   | No       | String        |
| ports            | Port bindings, for instance `3000:80`             | No       | List\<String> |
| env              | Environment variables, for instance `API_KEY=key` | No       | List\<String> |
| runAfterCreation | Start container once created                      | No       | boolean       |

Here's an example:
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

#### Complete example
This is how a complete configuration looks like:

```groovy
docker {
    image {
        dockerfile.set(project.file("Dockerfile"))
        buildContext.set(project.projectDir)
        tags.addAll(listOf("myImage:0.0.1"))
        // pull.set(true)
    }
    
    container {
        image.set("redis:7.0-alpine")
        name.set("my-redis")
        ports.addAll(listOf("6379:6379"))
        env.addAll(listOf("RANDOM_KEY=myrandomkey", "SOMETHING=value"))
        runAfterCreation.set(true)
    }
}
```
