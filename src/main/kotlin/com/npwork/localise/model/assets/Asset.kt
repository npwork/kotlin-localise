package com.npwork.localise.model.assets

data class Asset(
    val aliases: Aliases,
    val context: String,
    val id: String,
    val modified: String,
    val notes: String,
    val plurals: Int,
    val progress: Progress,
    val tags: List<String>,
    val type: String
)
