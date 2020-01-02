package com.fallgamlet.dnestrcinema.ui.movie.detail

import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.app.GlideApp
import com.fallgamlet.dnestrcinema.data.network.KinoTir
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.ui.ImageActivity
import com.fallgamlet.dnestrcinema.ui.holders.FieldHolder
import com.fallgamlet.dnestrcinema.ui.holders.MovieViewHolder
import com.fallgamlet.dnestrcinema.ui.movie.ImageRecyclerAdapter
import com.fallgamlet.dnestrcinema.utils.HttpUtils
import com.fallgamlet.dnestrcinema.utils.IntentUtils
import java.util.*

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var movieHolder: MovieViewHolder
    private lateinit var directorHolder: FieldHolder
    private lateinit var scenarioHolder: FieldHolder
    private lateinit var actorsHolder: FieldHolder
    private lateinit var ageLimitHolder: FieldHolder
    private lateinit var durationHolder: FieldHolder
    private lateinit var budgetHolder: FieldHolder
    private lateinit var genreHolder: FieldHolder
    private lateinit var countryHolder: FieldHolder

    @BindView(R.id.posterImageView)
    protected lateinit var posterImageView: ImageView
    @BindView(R.id.toolbar)
    protected lateinit var toolbar: Toolbar
    @BindView(R.id.shortInfoContainer)
    protected lateinit var shortInfoContainer: View
    @BindView(R.id.directorView)
    protected lateinit var directorView: View
    @BindView(R.id.scenarioView)
    protected lateinit var scenarioView: View
    @BindView(R.id.actorsView)
    protected lateinit var actorsView: View
    @BindView(R.id.ageLimitView)
    protected lateinit var ageLimitView: View
    @BindView(R.id.durationView)
    protected lateinit var durationView: View
    @BindView(R.id.budgetView)
    protected lateinit var budgetView: View
    @BindView(R.id.genreView)
    protected lateinit var genreView: View
    @BindView(R.id.countryView)
    protected lateinit var countryView: View
    @BindView(R.id.descriptionView)
    protected lateinit var descriptionView: TextView
    @BindView(R.id.imageList)
    protected lateinit var imageListView: RecyclerView
    @BindView(R.id.trailerBtn)
    protected lateinit var trailerBtn: View
    @BindView(R.id.buyTicketButton)
    protected lateinit var buyTicketButton: View

    private lateinit var viewModel: MovieDetailsViewModelImpl
    private lateinit var imageListAdapter: ImageRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinema_detail)

        ButterKnife.bind(this)

        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        val bundle = intent.extras
        val movieItem: MovieItem? = bundle?.getParcelable(ARG_MOVIE)

        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModelImpl::class.java)
        viewModel.setData(movieItem)

        val viewState = viewModel.viewState
        viewState.title.observe(this, Observer { title = it })
        viewState.pubDate.observe(this, Observer { setPubDate(it) })
        viewState.rooms.observe(this, Observer { setRooms(it) })
        viewState.duration.observe(this, Observer { setDuration(it) })
        viewState.director.observe(this, Observer { setDirector(it) })
        viewState.actors.observe(this, Observer { setActors(it) })
        viewState.scenario.observe(this, Observer { setScenario(it) })
        viewState.ageLimit.observe(this, Observer { setAgeLimit(it) })
        viewState.budget.observe(this, Observer { setBudget(it) })
        viewState.country.observe(this, Observer { setCountry(it) })
        viewState.genre.observe(this, Observer { setGenre(it) })
        viewState.description.observe(this, Observer { setDescription(it) })
        viewState.posterUrl.observe(this, Observer { setPosterUrl(it) })
        viewState.imageUrls.observe(this, Observer { setImageUrls(it) })
        viewState.trailerMovieUrls.observe(this, Observer { setTrailerUrls(it) })
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

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = null
        }

        buyTicketButton.isVisible = false

        initListView()
        initFieldHolders()
    }

    private fun initListView() {
        imageListAdapter = ImageRecyclerAdapter()

        imageListView.adapter = imageListAdapter
        imageListView.setHasFixedSize(false)
        imageListView.itemAnimator = DefaultItemAnimator()
        imageListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageListView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.left = 16
                }
            }
        })
    }

    private fun initFieldHolders() {
        movieHolder = MovieViewHolder(shortInfoContainer)
        directorHolder = FieldHolder(directorView)
        scenarioHolder = FieldHolder(scenarioView)
        actorsHolder = FieldHolder(actorsView)
        ageLimitHolder = FieldHolder(ageLimitView)
        durationHolder = FieldHolder(durationView)
        budgetHolder = FieldHolder(budgetView)
        genreHolder = FieldHolder(genreView)
        countryHolder = FieldHolder(countryView)
    }


    override fun setTitle(title: CharSequence?) {
        movieHolder.setTitle(title)
    }

    private fun setPubDate(date: Date?) {
        movieHolder.setPubDate(date)
    }

    private fun setRooms(rooms: Collection<CharSequence>?) {
        val roomsStr = rooms?.joinToString("\n") ?: ""
        movieHolder.setSchedule(roomsStr)

        val roomNames = viewModel.getRoomNames().toTypedArray()

        movieHolder.scheduleView.setOnClickListener {
            if (roomNames.isEmpty()) return@setOnClickListener

            if (roomNames.size == 1) {
                navigateToRoomView(roomNames.first())
            } else {
                showRoomSelectionDialog(roomNames)
            }
        }
    }

    private fun showRoomSelectionDialog(items: Array<String>) {
        AlertDialog.Builder(this, R.style.AppTheme_Dialog)
            .setTitle(R.string.label_choose_room)
            .setCancelable(true)
            .setSingleChoiceItems(items, -1) { dialog, i ->
                dialog.dismiss()
                navigateToRoomView(items[i])
            }
            .create()
            .show()
    }

    private fun navigateToRoomView(roomName: String?) {
        roomName ?: return

        var imgURL: String? = when {
            MovieItem.ROOM_BLUE.equals(roomName, ignoreCase = true) -> KinoTir.PATH_IMG_ROOM_BLUE
            MovieItem.ROOM_BORDO.equals(roomName, ignoreCase = true) -> KinoTir.PATH_IMG_ROOM_BORDO
            MovieItem.ROOM_DVD.equals(roomName, ignoreCase = true) -> KinoTir.PATH_IMG_ROOM_DVD
            else -> ""
        }

        if (imgURL.isNullOrBlank()) {
            return
        }

        val baseUrl = AppFacade.instance.requestFactory?.baseUrl
        imgURL = HttpUtils.getAbsoluteUrl(baseUrl, imgURL)
        ImageActivity.showActivity(this, imgURL)
    }

    private fun setDuration(value: CharSequence?) {
        val duration = value?.toString() ?: ""
        durationHolder.setDataAndVisible(getString(R.string.label_duration), duration)
    }

    private fun setGenre(v: CharSequence?) {
        val value = v?.toString()
        genreHolder.setDataAndVisible(getString(R.string.label_genre), value)
    }

    private fun setAgeLimit(v: CharSequence?) {
        val value = v?.toString()
        ageLimitHolder.setDataAndVisible(getString(R.string.label_agelimit), value)
    }

    private fun setCountry(v: CharSequence?) {
        val value = v?.toString()
        countryHolder.setDataAndVisible(getString(R.string.label_country), value)
    }

    private fun setDirector(v: CharSequence?) {
        val value = v?.toString()
        directorHolder.setDataAndVisible(getString(R.string.label_director), value)
    }

    private fun setScenario(v: CharSequence?) {
        val value = v?.toString()
        scenarioHolder.setDataAndVisible(getString(R.string.label_scenario), value)
    }

    private fun setActors(v: CharSequence?) {
        val value = v?.toString()
        actorsHolder.setDataAndVisible(getString(R.string.label_actors), value)
    }

    private fun setBudget(v: CharSequence?) {
        val value = v?.toString()
        budgetHolder.setDataAndVisible(getString(R.string.label_budget), value)
    }

    private fun setDescription(v: String?) {
        descriptionView.isVisible = !v.isNullOrBlank()
        descriptionView.text = HttpUtils.fromHtml(v)
    }

    private fun setPosterUrl(value: String?) {
        GlideApp.with(posterImageView)
            .load(value)
            .fallback(R.drawable.ic_photo_empty_240dp)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(posterImageView)
    }

    private fun setImageUrls(urls: List<String>?) {
        imageListView.isVisible = !urls.isNullOrEmpty()
        imageListAdapter.setData(urls)
    }

    private fun setTrailerUrls(urls: List<String>?) {
        trailerBtn.isVisible = !urls.isNullOrEmpty()
        trailerBtn.setOnClickListener {
            if (urls.isNullOrEmpty()) return@setOnClickListener

            if (urls.size == 1) {
                showTrainer(urls.first())
                return@setOnClickListener
            }

            val context = it.context
            val names = (urls.indices)
                .map { index -> "${context.getString(R.string.label_trailer)} ${index+1}" }
                .toTypedArray()

            AlertDialog.Builder(it.context, R.style.AppTheme_Dialog)
                .setItems(names) {dialog, which ->
                    dialog.dismiss()
                    showTrainer(urls[which])
                }
                .create()
                .show()
        }
    }

    private fun showTrainer(url: String?) {
        url ?: return
        IntentUtils.openUrl(this, url)
    }

    companion object {
        var ARG_MOVIE = "movie_item_now"
        var ARG_MOVIE_DETAIL = "movie_detail"
        var YOUTUBE = "youtube.com"
    }
}
