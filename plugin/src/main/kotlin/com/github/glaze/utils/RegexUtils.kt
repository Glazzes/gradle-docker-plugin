package com.github.glaze.utils

object RegexUtils {

    data class Ports(val hostPort: Int, val containerPort: Int)

    fun getPortsFromMapping(portMapping: String): Ports {
        val mappingRegex = Regex("(\\d{1,5}):(\\d{1,5})")
        val matchesMappingRegex = mappingRegex.matches(portMapping)

        if(!matchesMappingRegex) {
            throw IllegalArgumentException("Port mapping \"$portMapping\" is not valid")
        }

        val result = mappingRegex.matchEntire(portMapping)!!
        val hostPort = result.groups[1]!!.value.toInt()
        val containerPort = result.groups[2]!!.value.toInt()

        return Ports(hostPort, containerPort)
    }

}
