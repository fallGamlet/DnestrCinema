package com.fallgamlet.dnestrcinema;

import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.models.NewsItem;
import com.fallgamlet.dnestrcinema.mvp.models.TicketItem;
import com.fallgamlet.dnestrcinema.network.MapperFactory;
import com.fallgamlet.dnestrcinema.network.NetClient;
import com.fallgamlet.dnestrcinema.network.RequestFactory;
import com.fallgamlet.dnestrcinema.network.kinotir.KinotirMapperFactory;
import com.fallgamlet.dnestrcinema.network.kinotir.KinotirRequestFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class KinotirServerTest {
    private RequestFactory requestFactory = new KinotirRequestFactory();
    private MapperFactory mapperFactory = new KinotirMapperFactory();
    private NetClient netClient = new NetClient(requestFactory, mapperFactory);

    private String login = "fallgamlet@yandex.ru";
    private String password = "3nGggW3y";


    @Test
    public void testlogin() throws Exception {

        netClient.setLogin(login);
        netClient.setPassword(password);

        final BooleanResult result = login(login, password);

        Assert.assertTrue(result.value);
        Assert.assertNull(result.throwable);
    }

    @Test
    public void testGetTickets() throws Exception {

        BooleanResult loginResult = login(login, password);

        Assert.assertTrue(loginResult.value);

        final TicketsResult result = new TicketsResult();

        netClient.getTickets()
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        createConsumerTickets(result),
                        createConsumerThrowable(result)
                );

        Assert.assertNotNull(result.items);
        Assert.assertFalse(result.items.isEmpty());
        Assert.assertNull(result.throwable);
    }

    @Test
    public void testGetTodayMovies() throws Exception {

        final MoviesResult result = new MoviesResult();

        netClient.getTodayMovies()
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        createConsumerMovies(result),
                        createConsumerThrowable(result)
                );

        Assert.assertNotNull(result.items);
        Assert.assertFalse(result.items.isEmpty());
        Assert.assertNull(result.throwable);
    }

    @Test
    public void testGetSoonMovies() throws Exception {

        final MoviesResult result = new MoviesResult();

        netClient.getSoonMovies()
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        createConsumerMovies(result),
                        createConsumerThrowable(result)
                );

        Assert.assertNotNull(result.items);
        Assert.assertFalse(result.items.isEmpty());
        Assert.assertNull(result.throwable);
    }

    @Test
    public void testGetDetailMovies() throws Exception {

        final MovieDetailResult result = new MovieDetailResult();

        String path = "/filmy/chelovek-pauk-vozvraschenie-domoj/";

        netClient.getDetailMovies(path)
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        createConsumerMovieDetail(result),
                        createConsumerThrowable(result)
                );

        Assert.assertNotNull(result.value);
        Assert.assertNull(result.throwable);
    }

    @Test
    public void testGetNews() throws Exception {

        final NewsesResult result = new NewsesResult();

        netClient.getNews()
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        createConsumerNewses(result),
                        createConsumerThrowable(result)
                );

        Assert.assertNotNull(result.items);
        Assert.assertFalse(result.items.isEmpty());
        Assert.assertNull(result.throwable);
    }



    private BooleanResult login(String login, String password) {
        final BooleanResult result = new BooleanResult();

        netClient.login(login, password)
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        createConsumerBoolean(result),
                        createConsumerThrowable(result)
                );

        return result;
    }


    private Consumer<Throwable> createConsumerThrowable(final BaseResult result) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                result.throwable = throwable;
            }
        };
    }

    private Consumer<Boolean> createConsumerBoolean(final BooleanResult result) {
        return new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean value) throws Exception {
                result.value = value;
            }
        };
    }

    private Consumer<List<MovieItem>> createConsumerMovies(final MoviesResult result) {
        return new Consumer<List<MovieItem>>() {
            @Override
            public void accept(@NonNull List<MovieItem> items) throws Exception {
                result.items = items;
            }
        };
    }

    private Consumer<MovieItem> createConsumerMovieDetail(final MovieDetailResult result) {
        return new Consumer<MovieItem>() {
            @Override
            public void accept(@NonNull MovieItem item) throws Exception {
                result.value = item;
            }
        };
    }

    private Consumer<List<NewsItem>> createConsumerNewses(final NewsesResult result) {
        return new Consumer<List<NewsItem>>() {
            @Override
            public void accept(@NonNull List<NewsItem> items) throws Exception {
                result.items = items;
            }
        };
    }

    private Consumer<List<TicketItem>> createConsumerTickets(final TicketsResult result) {
        return new Consumer<List<TicketItem>>() {
            @Override
            public void accept(@NonNull List<TicketItem> items) throws Exception {
                result.items = items;
            }
        };
    }


    private class BaseResult {
        public Throwable throwable = null;
    }

    private class BooleanResult extends BaseResult {
        public boolean value = false;
    }

    private class MoviesResult extends BaseResult {
        public List<MovieItem> items;
    }

    private class MovieDetailResult extends BaseResult {
        public MovieItem value;
    }

    private class NewsesResult extends BaseResult {
        public List<NewsItem> items;
    }

    private class TicketsResult extends BaseResult {
        public List<TicketItem> items;
    }

}