package com.npwork.localise.api

import com.npwork.localise.model.assets.Asset
import com.npwork.localise.model.auth.AuthVerify
import com.npwork.localise.model.locale.Locale
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/api")
interface ApiService {
    @GET
    @Path("/locales")
    fun locales(): List<Locale>

    @GET
    @Path("/auth/verify")
    fun authVerify(): AuthVerify

    @GET
    @Path("/export/all.json")
    fun translations(): Map<Any, Any>

    @GET
    @Path("/assets")
    fun assets(): List<Asset>
}
