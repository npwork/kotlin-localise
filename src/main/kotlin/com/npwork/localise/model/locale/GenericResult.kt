package com.npwork.localise.model.locale

import com.fasterxml.jackson.annotation.JsonProperty

data class GenericResult<ResultType>(
        @JsonProperty("result")
        val result: ResultType,
        @JsonProperty("errors")
        val errors: String
)
