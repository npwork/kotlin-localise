package com.npwork.localise.example

import com.npwork.localise.LocoClient

fun main() {
    val client = LocoClient("<API_KEY>")
    val i18n = client.translations("en")
    println(i18n.t("my.another"))
}
