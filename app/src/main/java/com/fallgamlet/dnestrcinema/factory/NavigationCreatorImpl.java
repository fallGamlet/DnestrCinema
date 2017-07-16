package com.fallgamlet.dnestrcinema.factory;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpNavigationCreator;
import com.fallgamlet.dnestrcinema.mvp.models.NavigationItem;

/**
 * Created by fallgamlet on 15.07.17.
 */

public class NavigationCreatorImpl implements MvpNavigationCreator {

    @Override
    public NavigationItem getNavigationItem(int id) {
        NavigationItem item = null;
        switch (id) {
            case NavigationItem.NavigationId.TODAY:
                item = createTodayNavigation();
                break;
            case NavigationItem.NavigationId.SOON:
                item = createSoonNavigation();
                break;
            case NavigationItem.NavigationId.TICKETS:
                item = createTicketsNavigation();
                break;
            case NavigationItem.NavigationId.LOGIN:
                item = createLoginNavigation();
                break;
            case NavigationItem.NavigationId.NEWS:
                item = createNewsNavigation();
                break;
            case NavigationItem.NavigationId.ABOUT:
                item = createAboutNavigation();
                break;
            default:
                item = null;
                break;
        }
        return item;
    }


    private NavigationItem createTodayNavigation() {
        NavigationItem item = new NavigationItem(NavigationItem.NavigationId.TODAY);
        item.setTitleId(R.string.today);
        item.setIconResId(R.drawable.ic_local_movies_black_24dp);
        return item;
    }

    private NavigationItem createSoonNavigation() {
        NavigationItem item = new NavigationItem(NavigationItem.NavigationId.TODAY);
        item.setTitleId(R.string.soon);
        item.setIconResId(R.drawable.ic_watch_later_black_24dp);
        return item;
    }

    private NavigationItem createLoginNavigation() {
        NavigationItem item = new NavigationItem(NavigationItem.NavigationId.TODAY);
        item.setTitleId(R.string.title_login);
        item.setIconResId(R.drawable.ic_person_black_24dp);
        return item;
    }

    private NavigationItem createTicketsNavigation() {
        NavigationItem item = new NavigationItem(NavigationItem.NavigationId.TODAY);
        item.setTitleId(R.string.tickets);
        item.setIconResId(R.drawable.ic_local_offer_black_24dp);
        return item;
    }

    private NavigationItem createNewsNavigation() {
        NavigationItem item = new NavigationItem(NavigationItem.NavigationId.TODAY);
        item.setTitleId(R.string.news);
        item.setIconResId(R.drawable.ic_library_books_black_24dp);
        return item;
    }

    private NavigationItem createAboutNavigation() {
        NavigationItem item = new NavigationItem(NavigationItem.NavigationId.TODAY);
        item.setTitleId(R.string.about);
        item.setIconResId(R.drawable.ic_info_black_24dp);
        return item;
    }
}
