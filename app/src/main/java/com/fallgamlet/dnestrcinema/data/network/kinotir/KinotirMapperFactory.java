package com.fallgamlet.dnestrcinema.data.network.kinotir;

import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.domain.models.NewsItem;
import com.fallgamlet.dnestrcinema.domain.models.TicketItem;
import com.fallgamlet.dnestrcinema.data.network.Mapper;
import com.fallgamlet.dnestrcinema.data.network.MapperFactory;
import com.fallgamlet.dnestrcinema.data.network.kinotir.mappers.HtmlMovieDetailMapper;
import com.fallgamlet.dnestrcinema.data.network.kinotir.mappers.HtmlMoviesMapper;
import com.fallgamlet.dnestrcinema.data.network.kinotir.mappers.HtmlNewsMapper;
import com.fallgamlet.dnestrcinema.data.network.kinotir.mappers.HtmlTicketsMapper;
import com.fallgamlet.dnestrcinema.data.network.kinotir.mappers.JsonLoginMapper;

import java.util.List;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class KinotirMapperFactory implements MapperFactory {

    @Override
    public Mapper<String, Boolean> loginMapper() {
        return new JsonLoginMapper();
    }

    @Override
    public Mapper<String, List<MovieItem>> todayMoviesMapper() {
        return new HtmlMoviesMapper();
    }

    @Override
    public Mapper<String, List<MovieItem>> soonMoviesMapper() {
        return new HtmlMoviesMapper();
    }

    @Override
    public Mapper<String, MovieItem> detailMoviesMapper() {
        return new HtmlMovieDetailMapper();
    }

    @Override
    public Mapper<String, List<TicketItem>> ticketsMapper() {
        return new HtmlTicketsMapper();
    }

    @Override
    public Mapper<String, List<NewsItem>> newsMapper() {
        return new HtmlNewsMapper();
    }
}
