package com.fallgamlet.dnestrcinema.comparators;

import com.fallgamlet.dnestrcinema.domain.models.MovieItem;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class MovieDateTitleComparator implements Comparator <MovieItem> {

    @Override
    public int compare(MovieItem item1, MovieItem item2) {
        if (item1 == null) {
            if (item2 == null) { return 0; }
            else { return -1; }
        }
        if (item2 == null) { return 1; }

        Date date1 = item1.getPubDate();
        Date date2 = item2.getPubDate();

        if (date1 == null) {
            if (date2 == null) { return 0; }
            else { return -1; }
        }
        if (date2 == null) { return 1; }

        int dcomp = date1.compareTo(date2);
        if (dcomp != 0) {
            return dcomp;
        }

        String title1 = item1.getTitle();
        String title2 = item2.getTitle();

        if (title1 == null) {
            if (title2 == null) { return 0; }
            else { return -1; }
        }
        if (title2 == null) { return 1; }

        return title1.compareTo(title2);
    }

}
