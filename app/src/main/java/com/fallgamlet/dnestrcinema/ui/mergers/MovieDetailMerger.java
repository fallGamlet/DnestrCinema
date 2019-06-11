package com.fallgamlet.dnestrcinema.ui.mergers;

import com.fallgamlet.dnestrcinema.domain.models.MovieDetailItem;
import com.fallgamlet.dnestrcinema.utils.StringUtils;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class MovieDetailMerger implements Merger<MovieDetailItem> {

    @Override
    public void merge(MovieDetailItem to, MovieDetailItem from) {
        mergeActors(to, from);
        mergeAgeLimit(to, from);
        mergeBudget(to, from);
        mergeCountry(to, from);
        mergeDescription(to, from);
        mergeDirector(to, from);
        mergeGenre(to, from);
        mergeScenario(to, from);
        mergeImgUrls(to, from);
    }

    private void mergeActors(MovieDetailItem to, MovieDetailItem from) {
        if (StringUtils.INSTANCE.isEmpty(to.getActors())) {
            to.setActors(from.getActors());
        }
    }

    private void mergeAgeLimit(MovieDetailItem to, MovieDetailItem from) {
        if (StringUtils.INSTANCE.isEmpty(to.getAgeLimit())) {
            to.setAgeLimit(from.getAgeLimit());
        }
    }

    private void mergeBudget(MovieDetailItem to, MovieDetailItem from) {
        if (StringUtils.INSTANCE.isEmpty(to.getBudget())) {
            to.setBudget(from.getBudget());
        }
    }

    private void mergeCountry(MovieDetailItem to, MovieDetailItem from) {
        if (StringUtils.INSTANCE.isEmpty(to.getCountry())) {
            to.setCountry(from.getCountry());
        }
    }

    private void mergeDescription(MovieDetailItem to, MovieDetailItem from) {
        if (StringUtils.INSTANCE.isEmpty(to.getDescription())) {
            to.setDescription(from.getDescription());
        }
    }

    private void mergeDirector(MovieDetailItem to, MovieDetailItem from) {
        if (StringUtils.INSTANCE.isEmpty(to.getDirector())) {
            to.setDirector(from.getDirector());
        }
    }

    private void mergeGenre(MovieDetailItem to, MovieDetailItem from) {
        if (StringUtils.INSTANCE.isEmpty(to.getGenre())) {
            to.setGenre(from.getGenre());
        }
    }

    private void mergeScenario(MovieDetailItem to, MovieDetailItem from) {
        if (StringUtils.INSTANCE.isEmpty(to.getScenario())) {
            to.setScenario(from.getScenario());
        }
    }

    private void mergeImgUrls(MovieDetailItem to, MovieDetailItem from) {
        to.getImgUrls().addAll(from.getImgUrls());
    }

}
