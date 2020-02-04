package com.npwork.localise

import java.time.temporal.ChronoUnit

// Java 8 can't conver
data class CacheConfig(val file: String,
                       val duration: Long = 10,
                       val unit: ChronoUnit = ChronoUnit.MINUTES)
