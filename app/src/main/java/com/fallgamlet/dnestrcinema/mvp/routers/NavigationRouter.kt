package com.fallgamlet.dnestrcinema.mvp.routers


interface NavigationRouter {
    fun showToday()

    fun showSoon()

    fun showAbout()

    fun showNews()

    fun showMovieDetail(movieLink: String)
}
