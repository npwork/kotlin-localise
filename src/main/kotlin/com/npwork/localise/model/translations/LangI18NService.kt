package com.npwork.localise.model.translations

class LangI18NService(val translations: AllTranslations, val lang: String) {
    fun tObject(key: String): Any? = translations[lang]?.get(key)

    fun t(key: String): String? = translations[lang]?.get(key)?.toString()
}
