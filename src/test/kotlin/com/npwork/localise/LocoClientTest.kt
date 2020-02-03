package com.npwork.localise

import com.npwork.localise.model.translations.I18NService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class LocoClientTest {
    val client = LocoClient(TestProps.TEST_API_KEY)

    @Test
    @DisplayName("Valid")
    fun valid() {
        verifyEnTranslations(client.translations("en"))
        verifyRuTranslations(client.translations("ru"))
        verifyInternal(client.translations("en"))
    }

    @Test
    @DisplayName("No translation")
    fun noTranslation() {
        assertThat(client.translations("en").t("not_exist")).isNull()
    }

    @Test
    @DisplayName("Valid auth")
    fun validAuth() {
        assertThat(client.authVerify()?.project?.id).isEqualTo(83382)
    }

    @Test
    @DisplayName("Wrong auth")
    fun wrongAuth() {
        val wrongKey = LocoClient("wrong")
        assertThat(wrongKey.authVerify()).isNull()
    }

    private fun verifyEnTranslations(i18n: I18NService) {
        assertThat(i18n.t("hello")).isEqualTo("hello")
        assertThat(i18n.t("world")).isEqualTo("world")
    }

    private fun verifyRuTranslations(i18n: I18NService) {
        assertThat(i18n.t("hello")).isEqualTo("привет")
        assertThat(i18n.t("world")).isEqualTo("мир")
    }

    private fun verifyInternal(i18n: I18NService) {
        assertThat(i18n.t("common.internal")).isEqualTo("internal")
    }
}
