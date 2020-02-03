package com.npwork.localise

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import java.io.File

object FileCacheService {
    fun saveToFile(cacheFile: String?, content: String) {
        if (cacheFile != null)
            File(cacheFile).writeText(content)
    }

    fun tryReadFromFile(cacheFile: String?): Pair<String, JsonNode>? {
        if (cacheFile == null || !File(cacheFile).exists())
            return null

        val responseAsString = File(cacheFile).readText(Charsets.UTF_8)
        if (responseAsString == "") {
            return null
        }

        return try {
            Pair(responseAsString, LocoClient.mapper.readTree(responseAsString))
        } catch (e: JsonProcessingException) {
            null
        }
    }
}
