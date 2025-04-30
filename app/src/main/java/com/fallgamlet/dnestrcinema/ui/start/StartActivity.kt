package com.fallgamlet.dnestrcinema.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.fallgamlet.dnestrcinema.app.AppFacade.Companion.instance
import com.fallgamlet.dnestrcinema.dagger.getAppComponent
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailActivity
import javax.inject.Inject

class StartActivity : AppCompatActivity(), NavigationRouter {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(
            ComposeView(this).apply {
                setContent {
                    AppScreen(
                        viewModelFactory = viewModelFactory,
                        context = { this@StartActivity }
                    )
                }
            }
        )
    }

//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId) {
//            R.id.actionShareApp -> {
//                shareApp(this)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onPause() {
        instance.navigationRouter = null
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        instance.navigationRouter = this
    }

    override fun showMovieDetail(movieLink: String) {
        val bundle = Bundle().apply { putString("movie_link", movieLink) }
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
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
