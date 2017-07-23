package com.fallgamlet.dnestrcinema.mergers;

import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.utils.StringUtils;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class MovieMerger implements Merger <MovieItem> {

    MovieDetailMerger detailMerger;

    public MovieMerger() {
        detailMerger = new MovieDetailMerger();
    }


    @Override
    public void merge(MovieItem to, MovieItem from) {
        mergeTitle(to, from);
        mergePubDate(to, from);
        mergeLink(to, from);
        mergeBuyTicketLink(to, from);
        mergePosterUrl(to, from);
        mergeDuration(to, from);
        mergeSchedulers(to, from);
        mergeTrailers(to, from);

        detailMerger.merge(to.getDetail(), from.getDetail());
    }

    private void mergeTitle(MovieItem to, MovieItem from) {
        if (StringUtils.isEmpty(to.getTitle())) {
            to.setTitle(from.getTitle());
        }
    }

    private void mergePubDate(MovieItem to, MovieItem from) {
        if (to.getPubDate() == null) {
            to.setPubDate(from.getPubDate());
        }
    }

    private void mergeLink(MovieItem to, MovieItem from) {
        if (StringUtils.isEmpty(to.getLink())) {
            to.setLink(from.getLink());
        }
    }

    private void mergeBuyTicketLink(MovieItem to, MovieItem from) {
        if (StringUtils.isEmpty(to.getBuyTicketLink())) {
            to.setBuyTicketLink(from.getBuyTicketLink());
        }
    }

    private void mergePosterUrl(MovieItem to, MovieItem from) {
        if (StringUtils.isEmpty(to.getPosterUrl())) {
            to.setPosterUrl(from.getPosterUrl());
        }
    }

    private void mergeDuration(MovieItem to, MovieItem from) {
        if (StringUtils.isEmpty(to.getDuration())) {
            to.setDuration(from.getDuration());
        }
    }

    private void mergeSchedulers(MovieItem to, MovieItem from) {
        if (to.getSchedules().isEmpty()) {
            to.getSchedules().addAll(from.getSchedules());
        }
    }

    private void mergeTrailers(MovieItem to, MovieItem from) {
        if (to.getTrailerUrlSet().isEmpty()) {
            to.getTrailerUrlSet().addAll(from.getTrailerUrlSet());
        }
    }
}
