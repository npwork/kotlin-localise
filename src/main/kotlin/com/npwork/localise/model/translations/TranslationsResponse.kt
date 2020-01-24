package com.npwork.localise.model.translations

typealias AllTranslations = Map<String, Map<String, Any>>

data class TranslationsResponse(val all: AllTranslations, val responseHash: String)
