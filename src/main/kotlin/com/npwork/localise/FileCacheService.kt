package com.npwork.localise

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class FileMeta(val timestamp: Long) {
    @JsonIgnore
    fun getLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
}

object FileCacheService {
    fun saveToFile(cacheConfig: CacheConfig?, content: String) {
        if (cacheConfig != null) {
            File(cacheConfig.file).writeText(content)
            File(metaFile(cacheConfig.file)).writeText(objectMapper.writeValueAsString(FileMeta(System.currentTimeMillis())))
        }
    }

    fun tryReadFromFile(cacheConfig: CacheConfig?): Pair<String, JsonNode>? {
        if (cacheConfig == null || !File(cacheConfig.file).exists())
            return null

        val meta = readMetaFile(cacheConfig)
        if (meta != null) {
            val until = meta.getLocalDateTime().plus(cacheConfig.duration, cacheConfig.unit)
            if (LocalDateTime.now().isAfter(until))
                return null
        }

        val responseAsString = File(cacheConfig.file).readText(Charsets.UTF_8)
        if (responseAsString == "") {
            return null
        }

        return try {
            Pair(responseAsString, objectMapper.readTree(responseAsString))
        } catch (e: JsonProcessingException) {
            null
        }
    }

    private fun metaFile(cacheFile: String?) = "$cacheFile.meta"

    private fun readMetaFile(cacheConfig: CacheConfig?): FileMeta? {
        return if (cacheConfig == null || !File(metaFile(cacheConfig.file)).exists())
            null
        else
            objectMapper.readValue(File(metaFile(cacheConfig.file)).readText(Charsets.UTF_8))
    }
}
