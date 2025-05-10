package com.fallgamlet.dnestrcinema.ui.start

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.dagger.getAppComponent
import com.fallgamlet.dnestrcinema.ui.navigation.NavigationActionsHolder
import com.fallgamlet.dnestrcinema.ui.navigation.Navigator
import com.fallgamlet.dnestrcinema.ui.navigation.TopLevelRoute
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.AboutDestination
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.NewsesDestination
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.SoonMoviesDestination
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.TodayMoviesDestination
import javax.inject.Inject

class StartActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationActionsHolder: NavigationActionsHolder

    @Inject
    lateinit var navigator: Navigator

    private val navItems: List<TopLevelRoute> by lazy {
        listOf(
            TopLevelRoute(
                destination = TodayMoviesDestination,
                title = getString(R.string.today),
                iconResId = R.drawable.ic_local_movies_black_24dp,
            ),
            TopLevelRoute(
                destination = SoonMoviesDestination,
                title = getString(R.string.soon),
                iconResId = R.drawable.ic_watch_later_black_24dp,
            ),
            TopLevelRoute(
                destination = NewsesDestination,
                title = getString(R.string.news),
                iconResId = R.drawable.ic_library_books_black_24dp,
            ),
            TopLevelRoute(
                destination = AboutDestination,
                title = getString(R.string.about),
                iconResId = R.drawable.ic_info_black_24dp,
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(
            ComposeView(this).apply {
                setContent {
                    AppScreen(
                        viewModelFactory = viewModelFactory,
                        navItems = navItems,
                        navigationActionsHolder = navigationActionsHolder,
                    )
                }
            }
        )
    }

//    override fun showBuyTicket(movieItem: MovieItem?) {
//        movieItem ?: return
//        val baseUrl = instance.requestFactory!!.baseUrl
//        val url = getAbsoluteUrl(baseUrl, movieItem.buyTicketLink) ?: return
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse(url)
//        startActivity(intent)
//    }
}
