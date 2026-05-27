package org.six.series.ui.components.main

import kotlinx.serialization.Serializable

object MainRoutes {
    const val Principal    = "main"
    const val Movies       = "movies"
    const val Series       = "series"
    const val Profile      = "profile"
    const val Search       = "search"
    const val Genres       = "genres"
    const val Subscription = "subscription"
    const val Detail = "detail"
    fun detailRoute(id: String) = "detail/$id"
}

@Serializable
data class DetailRoute(val contentId: String)