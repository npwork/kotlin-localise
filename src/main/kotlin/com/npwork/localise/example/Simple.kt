package com.npwork.localise.example

import com.npwork.localise.LocoClient

fun main() {
    val client = LocoClient("qqcKaJTp40VcLC8TuVpNlGJySbnQwsLO")
    val i18n = client.translations("en")
    println(i18n.t("common.email"))
}
