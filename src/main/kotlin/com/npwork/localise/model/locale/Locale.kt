package com.npwork.localise.model.locale

data class Locale(
        val code: String,
        val name: String,
        val native: Boolean,
        val plurals: Plurals,
        val progress: Progress,
        val source: Boolean
)
