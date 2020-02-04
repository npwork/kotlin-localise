package com.npwork.localise

import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.temporal.ChronoUnit

class CacheTest {
    @Test
    @DisplayName("Single request")
    fun single() {
        val client = LocoClient(TestProps.TEST_API_KEY)
        val spy = spyk(client.apiService)
        client.apiService = spy

        client.translations("en")
        client.translations("en")

        verify(exactly = 1) { spy.translations() }

        confirmVerified(spy)
    }

    @Test
    @DisplayName("Fetches after cache time")
    fun afterCacheTime() {
        val filePath = "${System.getProperty("java.io.tmpdir")}${System.currentTimeMillis()}"
        val client = LocoClient(apiKey = TestProps.TEST_API_KEY, cacheConfig = CacheConfig(file = filePath, duration = 0, unit = ChronoUnit.NANOS))
        val spy = spyk(client.apiService)
        client.apiService = spy

        client.translations("en")
        client.translations("en")

        verify(exactly = 2) { spy.translations() }

        confirmVerified(spy)
    }
}
