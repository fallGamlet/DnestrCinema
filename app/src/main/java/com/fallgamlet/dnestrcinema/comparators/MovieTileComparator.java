package com.fallgamlet.dnestrcinema.comparators;

import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;

import java.util.Comparator;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class MovieTileComparator implements Comparator <MovieItem> {

    @Override
    public int compare(MovieItem item1, MovieItem item2) {
        if (item1 == null) {
            if (item2 == null) { return 0; }
            else { return -1; }
        }
        if (item2 == null) { return 1; }

        String value1 = item1.getTitle();
        String value2 = item2.getTitle();

        if (value1 == null) {
            if (value2 == null) { return 0; }
            else { return -1; }
        }
        if (value2 == null) { return 1; }

        return value1.compareTo(value2);
    }

}
