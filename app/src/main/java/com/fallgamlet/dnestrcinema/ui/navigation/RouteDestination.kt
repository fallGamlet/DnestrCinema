package com.fallgamlet.dnestrcinema.ui.navigation


data class RouteDestination(
    val name: String,
    val route: String,
) {
    companion object {
        val TodayMovies = RouteDestination(
            name = "Today movies",
            route = "today_movies",
        )

        val SoonMovies = RouteDestination(
            name = "Soon movies",
            route = "soon_movies",
        )

        val MovieDetails = RouteDestination(
            name = "MovieDetails",
            route = "movie_details",
        )

        val Newses = RouteDestination(
            name = "Newses",
            route = "newses",
        )

        val About = RouteDestination(
            name = "About",
            route = "about",
        )
    }
}
