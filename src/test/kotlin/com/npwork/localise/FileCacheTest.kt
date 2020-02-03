package com.npwork.localise

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.System.currentTimeMillis
import java.lang.System.getProperty

class FileCacheTest {
    @Test
    @DisplayName("Saves to file after request")
    fun saveToFile() {
        val filePath = "${getProperty("java.io.tmpdir")}${currentTimeMillis()}"
        val client = LocoClient(TestProps.TEST_API_KEY, cacheFile = filePath)
        client.translations("en")

        checkFileContent(filePath)
    }

    @Test
    @DisplayName("Empty file")
    fun emptyFile() {
        val filePath = "${getProperty("java.io.tmpdir")}${currentTimeMillis()}"
        File(filePath).createNewFile()

        val client = LocoClient(TestProps.TEST_API_KEY, cacheFile = filePath)
        client.translations("en")

        checkFileContent(filePath)
    }

    @Test
    @DisplayName("Wrong format file")
    fun wrongFormat() {
        val filePath = "${getProperty("java.io.tmpdir")}${currentTimeMillis()}"
        File(filePath).writeText("hello world")

        val client = LocoClient(TestProps.TEST_API_KEY, cacheFile = filePath)
        client.translations("en")

        checkFileContent(filePath)
    }

    private fun checkFileContent(filePath: String) {
        val fileContent = File(filePath).readText(Charsets.UTF_8)
        val expectedContent = FileCacheTest::class.java.getResource("/response_fixture.json").readText().trim()

        assertThat(fileContent).isEqualTo(expectedContent)
    }
}
