package com.fallgamlet.dnestrcinema.ui.navigation

import com.fallgamlet.dnestrcinema.R

sealed class TopLevelRoute(
    val destination: RouteDestination,
    val iconResId: Int
) {
    data object TodayMovies : TopLevelRoute(
        destination = RouteDestination.TodayMovies,
        iconResId = R.drawable.ic_local_movies_black_24dp,
    )

    data object SoonMovies : TopLevelRoute(
        destination = RouteDestination.SoonMovies,
        iconResId = R.drawable.ic_watch_later_black_24dp,
    )

    data object Newses : TopLevelRoute(
        destination = RouteDestination.Newses,
        iconResId = R.drawable.ic_library_books_black_24dp,
    )

    data object About : TopLevelRoute(
        destination = RouteDestination.About,
        iconResId = R.drawable.ic_info_black_24dp,
    )
}
