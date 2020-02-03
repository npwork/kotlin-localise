package com.npwork.localise

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.wnameless.json.flattener.JsonFlattener
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.hash.Hashing
import com.npwork.localise.api.ApiService
import com.npwork.localise.model.auth.AuthVerify
import com.npwork.localise.model.translations.I18NService
import com.npwork.localise.model.translations.TranslationsResponse
import si.mazi.rescu.ClientConfig
import si.mazi.rescu.JacksonConfigureListener
import si.mazi.rescu.RestProxyFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import javax.ws.rs.HeaderParam

class LocoClient(
        val apiKey: String,
        val cacheFile: String? = null,
        val cacheDuration: Long = 10,
        val cacheUnit: TimeUnit = TimeUnit.MINUTES
) {
    private val config = ClientConfig().also {
        it.add(HeaderParam::class.java, "Authorization", "Loco $apiKey")
        it.jacksonConfigureListener = JacksonConfigureListener { objectMapper -> objectMapper!!.registerModules(KotlinModule()) }
    }

    internal var apiService: ApiService = RestProxyFactory.createProxy(ApiService::class.java, "https://localise.biz", config)

    private val cache = CacheBuilder.newBuilder()
            .expireAfterWrite(cacheDuration, cacheUnit)
            .build(object : CacheLoader<String, TranslationsResponse>() {
                override fun load(key: String) = loadAllTranslations()
            })

    fun translations(lang: String = "en"): I18NService = I18NService(cache.get(apiKey).all, lang)

    data class Response(val asString: String, val asJson: JsonNode)

    private fun loadAllTranslations(): TranslationsResponse {
        var data = FileCacheService.tryReadFromFile(cacheFile)
        if (data == null) {
            data = getTranslations()

            FileCacheService.saveToFile(cacheFile, data!!.first)
        }

        val responseHash = Hashing.sha256()
                .hashString(data.first, StandardCharsets.UTF_8)
                .toString()

        return TranslationsResponse(
                responseHash = responseHash,
                all = data.second.fields().asSequence().map { it.key to JsonFlattener.flattenAsMap(it.value.toString()) }.toMap()
        )
    }

    private fun getTranslations(): Pair<String, JsonNode>? {
        val resp = apiService.translations()
        val responseAsString = mapper.writeValueAsString(resp)
        return Pair(responseAsString, mapper.readTree(responseAsString))
    }

    @Suppress("TooGenericExceptionCaught")
    fun authVerify(): AuthVerify? {
        return try {
            apiService.authVerify()
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        val mapper = ObjectMapper().registerModules(KotlinModule())
    }
}
