package com.fallgamlet.dnestrcinema.ui.start;

import com.fallgamlet.dnestrcinema.ui.about.AboutFactory;
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonFactory;
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayFactory;
import com.fallgamlet.dnestrcinema.ui.news.NewsFactory;
import com.fallgamlet.dnestrcinema.ui.tickets.TicketsFactory;

/**
 * Created by fallgamlet on 07.07.17.
 */

public class NodeContainer {

    private TodayFactory todayFactory;
    private SoonFactory soonFactory;
    private TicketsFactory ticketsFactory;
    private NewsFactory newsFactory;
    private AboutFactory aboutFactory;

    public NodeContainer() {
        todayFactory = new TodayFactory();
        soonFactory = new SoonFactory();
        ticketsFactory = new TicketsFactory();
        newsFactory = new NewsFactory();
        aboutFactory = new AboutFactory();
    }

    public TodayFactory getTodayFactory() {
        return todayFactory;
    }

    public SoonFactory getSoonFactory() {
        return soonFactory;
    }

    public TicketsFactory getTicketsFactory() {
        return ticketsFactory;
    }

    public NewsFactory getNewsFactory() {
        return newsFactory;
    }

    public AboutFactory getAboutFactory() {
        return aboutFactory;
    }
}
