package com.fallgamlet.dnestrcinema.factory

import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.mvp.factory.MvpNavigationCreator
import com.fallgamlet.dnestrcinema.domain.models.NavigationItem


class NavigationCreatorImpl : MvpNavigationCreator {

    override fun getNavigationItem(id: Int): NavigationItem? {
        return when (id) {
            NavigationItem.NavigationId.TODAY -> createTodayNavigation()
            NavigationItem.NavigationId.SOON -> createSoonNavigation()
            NavigationItem.NavigationId.TICKETS -> createTicketsNavigation()
            NavigationItem.NavigationId.LOGIN -> createLoginNavigation()
            NavigationItem.NavigationId.NEWS -> createNewsNavigation()
            NavigationItem.NavigationId.ABOUT -> createAboutNavigation()
            else -> null
        }
    }


    private fun createTodayNavigation(): NavigationItem {
        val item = NavigationItem(NavigationItem.NavigationId.TODAY)
        item.titleId = R.string.today
        item.iconResId = R.drawable.ic_local_movies_black_24dp
        return item
    }

    private fun createSoonNavigation(): NavigationItem {
        val item = NavigationItem(NavigationItem.NavigationId.SOON)
        item.titleId = R.string.soon
        item.iconResId = R.drawable.ic_watch_later_black_24dp
        return item
    }

    private fun createLoginNavigation(): NavigationItem {
        val item = NavigationItem(NavigationItem.NavigationId.LOGIN)
        item.titleId = R.string.title_login
        item.iconResId = R.drawable.ic_person_black_24dp
        return item
    }

    private fun createTicketsNavigation(): NavigationItem {
        val item = NavigationItem(NavigationItem.NavigationId.TICKETS)
        item.titleId = R.string.tickets
        item.iconResId = R.drawable.ic_tickets_24dp
        return item
    }

    private fun createNewsNavigation(): NavigationItem {
        val item = NavigationItem(NavigationItem.NavigationId.NEWS)
        item.titleId = R.string.news
        item.iconResId = R.drawable.ic_library_books_black_24dp
        return item
    }

    private fun createAboutNavigation(): NavigationItem {
        val item = NavigationItem(NavigationItem.NavigationId.ABOUT)
        item.titleId = R.string.about
        item.iconResId = R.drawable.ic_info_black_24dp
        return item
    }
}
