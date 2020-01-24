package com.npwork.localise.model.locale

data class Progress(
    val flagged: Int,
    val translated: Int,
    val untranslated: Int,
    val words: Int
)
