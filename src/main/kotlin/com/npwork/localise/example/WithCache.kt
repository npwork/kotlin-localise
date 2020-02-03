package com.npwork.localise.example

import com.npwork.localise.LocoClient

fun main() {
    val client = LocoClient(
            apiKey = "qqcKaJTp40VcLC8TuVpNlGJySbnQwsLO",
            cacheFile = "${System.getProperty("java.io.tmpdir")}i18n.json"
    )

    val i18n = client.translations("en")
    println(i18n.t("common.email"))
}
