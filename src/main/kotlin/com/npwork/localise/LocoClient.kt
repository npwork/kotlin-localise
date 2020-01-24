package com.npwork.localise

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.wnameless.json.flattener.JsonFlattener
import com.google.common.hash.Hashing
import com.npwork.localise.api.ApiService
import com.npwork.localise.model.assets.Asset
import com.npwork.localise.model.locale.Locale
import com.npwork.localise.model.translations.LangI18NService
import com.npwork.localise.model.translations.TranslationsResponse
import si.mazi.rescu.ClientConfig
import si.mazi.rescu.JacksonConfigureListener
import si.mazi.rescu.RestProxyFactory
import java.nio.charset.StandardCharsets
import javax.ws.rs.HeaderParam

class LocoClient(val apiKey: String) {
    val mapper = ObjectMapper().registerModules(KotlinModule())

    companion object {
        const val URL = "https://localise.biz/api"
        const val LOCALE_PATH = "/locales"
        const val TRANSLATION_PATH = "/export/all.json"
    }

    private val config = ClientConfig().also {
        it.add(HeaderParam::class.java, "Authorization", "Loco $apiKey")
        it.jacksonConfigureListener = JacksonConfigureListener { objectMapper -> objectMapper!!.registerModules(KotlinModule()) }
    }

    private val apiService: ApiService = RestProxyFactory.createProxy(ApiService::class.java, "https://localise.biz", config)

    init {
        apiService.authVerify()
    }

    fun translations(lang: String): LangI18NService = LangI18NService(allTranslations().all, lang)

    fun allTranslations(): TranslationsResponse {
        val resp = apiService.translations()
        val responseAsString = mapper.writeValueAsString(resp)
        val responseAsJson = mapper.readTree(responseAsString)

        val responseHash = Hashing.sha256()
                .hashString(responseAsString, StandardCharsets.UTF_8)
                .toString()

        return TranslationsResponse(
                responseHash = responseHash,
                all = responseAsJson.fields().asSequence().map { it.key to JsonFlattener.flattenAsMap(it.value.toString()) }.toMap()
        )
    }

    fun locales(): List<Locale> = apiService.locales()

    fun assets(): List<Asset> = apiService.assets()
}
