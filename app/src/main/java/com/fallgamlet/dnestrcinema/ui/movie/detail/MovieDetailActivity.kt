package com.fallgamlet.dnestrcinema.ui.movie.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.dagger.getAppComponent
import com.fallgamlet.dnestrcinema.ui.utils.showError
import com.fallgamlet.dnestrcinema.utils.IntentUtils
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<MovieDetailsViewModel> { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
        val labels = getLabels()
        setContentView(
            ComposeView(this).apply {
                setContent {
                    val movieState = viewModel.movieState.collectAsState(MovieDetailsVo())
                    MovieDetailsScreen(
                        movie = movieState.value,
                        labels = labels,
                        trailerAction = { url ->
                            IntentUtils.openUrl(this@MovieDetailActivity, url)
                        },
                        backAction = {
                            onBackPressed()
                        }
                    )
                }
            }
        )

        viewModel.loadData(intent.extras?.getString("movie_link") ?: "")
        lifecycleScope.launch {
            viewModel.errorsState.filterNotNull().collect { error ->
                showError(error)
            }
        }
    }

    private fun getLabels(): MovieDetailsLabels {
        return MovieDetailsLabels(
            duration = getString(R.string.label_duration),
            director = getString(R.string.label_director),
            scenario = getString(R.string.label_scenario),
            actors = getString(R.string.label_actors),
            budget = getString(R.string.label_budget),
            ageLimit = getString(R.string.label_agelimit),
            country = getString(R.string.label_country),
            genre = getString(R.string.label_genre),
            trailer = getString(R.string.label_trailer),
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val res: Boolean
        when (id) {
            android.R.id.home -> {
                onBackPressed()
                res = true
            }
            else -> res = super.onOptionsItemSelected(item)
        }
        return res
    }

//    private fun setRooms(rooms: Collection<CharSequence>?) {
//        val roomsStr = rooms?.joinToString("\n") ?: ""
//        movieHolder.setSchedule(roomsStr)
//
//        val roomNames = viewModel.getRoomNames().toTypedArray()
//
//        movieHolder.scheduleView.setOnClickListener {
//            if (roomNames.isEmpty()) return@setOnClickListener
//
//            if (roomNames.size == 1) {
//                navigateToRoomView(roomNames.first())
//            } else {
//                showRoomSelectionDialog(roomNames)
//            }
//        }
//    }
//
//    private fun showRoomSelectionDialog(items: Array<String>) {
//        AlertDialog.Builder(this, R.style.AppTheme_Dialog)
//            .setTitle(R.string.label_choose_room)
//            .setCancelable(true)
//            .setSingleChoiceItems(items, -1) { dialog, i ->
//                dialog.dismiss()
//                navigateToRoomView(items[i])
//            }
//            .create()
//            .show()
//    }
//
//    private fun navigateToRoomView(roomName: String?) {
//        roomName ?: return
//
//        var imgURL: String? = when {
//            MovieItem.ROOM_BLUE.equals(roomName, ignoreCase = true) -> KinoTir.PATH_IMG_ROOM_BLUE
//            MovieItem.ROOM_BORDO.equals(roomName, ignoreCase = true) -> KinoTir.PATH_IMG_ROOM_BORDO
//            MovieItem.ROOM_DVD.equals(roomName, ignoreCase = true) -> KinoTir.PATH_IMG_ROOM_DVD
//            else -> ""
//        }
//
//        if (imgURL.isNullOrBlank()) {
//            return
//        }
//
//        val baseUrl = AppFacade.instance.requestFactory?.baseUrl
//        imgURL = HttpUtils.getAbsoluteUrl(baseUrl, imgURL)
//        ImageActivity.showActivity(this, imgURL)
//    }
//
//    private fun setTrailerUrls(urls: List<String>?) {
//        trailerBtn.isVisible = !urls.isNullOrEmpty()
//        trailerBtn.setOnClickListener {
//            if (urls.isNullOrEmpty()) return@setOnClickListener
//
//            if (urls.size == 1) {
//                showTrailer(urls.first())
//                return@setOnClickListener
//            }
//
//            val context = it.context
//            val names = (urls.indices)
//                .map { index -> "${context.getString(R.string.label_trailer)} ${index+1}" }
//                .toTypedArray()
//
//            AlertDialog.Builder(it.context, R.style.AppTheme_Dialog)
//                .setItems(names) {dialog, which ->
//                    dialog.dismiss()
//                    showTrailer(urls[which])
//                }
//                .create()
//                .show()
//        }
//    }
//
//    private fun showTrailer(url: String?) {
//        url ?: return
//        IntentUtils.openUrl(this, url)
//    }
}
