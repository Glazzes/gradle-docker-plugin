package com.github.glaze.common

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import java.time.Duration

object Client {

    private var client: DockerClient? = null

    fun getInstance(): DockerClient {
        if(client != null) {
            return client!!
        }

        val clientConfig = DefaultDockerClientConfig.Builder()
            .build()

        val httpClient = ApacheDockerHttpClient.Builder()
            .dockerHost(clientConfig.dockerHost)
            .sslConfig(clientConfig.sslConfig)
            .responseTimeout(Duration.ofMinutes(1L))
            .connectionTimeout(Duration.ofMinutes(1L))
            .maxConnections(100)
            .build()

        return DockerClientImpl.getInstance(clientConfig, httpClient)
    }

}
