package com.fallgamlet.dnestrcinema.network;

import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.models.NewsItem;
import com.fallgamlet.dnestrcinema.mvp.models.TicketItem;

import java.util.List;

/**
 * Created by fallgamlet on 08.07.17.
 */

public interface MapperFactory {

    Mapper<String, Boolean> loginMapper();

    Mapper<String, List<MovieItem>> todayMoviesMapper();

    Mapper<String, List<MovieItem>> soonMoviesMapper();

    Mapper<String, MovieItem> detailMoviesMapper();

    Mapper<String, List<TicketItem>> ticketsMapper();

    Mapper<String, List<NewsItem>> newsMapper();

}
