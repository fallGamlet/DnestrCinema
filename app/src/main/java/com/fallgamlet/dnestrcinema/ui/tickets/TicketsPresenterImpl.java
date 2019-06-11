package com.fallgamlet.dnestrcinema.ui.tickets;

import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.domain.models.TicketItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;
import com.fallgamlet.dnestrcinema.mvp.routers.LoginRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketsView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class TicketsPresenterImpl
    extends
        BasePresenter <MvpTicketsView>
    implements
        MvpTicketsPresenter,
        LoginRouter.LoginRouterListener
{

    private List<TicketItem> ticketItems;
    private LoginRouter router;


    public TicketsPresenterImpl() {
        ticketItems = getEmptyList();
    }


    @Override
    public void bindView(MvpTicketsView view) {
        super.bindView(view);
        if (ticketItems.isEmpty()) {
            loadData();
        }
        else {
            showData();
        }
    }

    @Override
    public void loadData() {
        boolean isLogin = AppFacade.getInstance().getNetClient().isLogin();

        if (isLogin) {
            loadTickets();
        }
        else {
            login();
        }
    }

    private void login() {
        setLoading(true);

        AppFacade.getInstance()
                .getNetClient()
                .login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        value -> {
                            setLoading(false);
                            if (value) {
                                loadTickets();
                            }
                            else {
                                navigateToLogin();
                            }

                        },
                        throwable -> {
                            setLoading(false);
                            navigateToLogin();
                        }
                );
    }

    private void loadTickets() {
        setLoading(true);

        AppFacade.getInstance()
                .getNetClient()
                .getTickets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        ticketItems -> {
                            setLoading(false);
                            updateData(ticketItems);
                        },
                        throwable -> {
                            setLoading(false);
                            updateData(getEmptyList());
                        }
                );
    }

    private void setLoading(boolean value) {
        if(!isViewBinded()) {
            return;
        }

        if (value) {
            getView().showLoading();
        }
        else {
            getView().hideLoading();
        }
    }

    private void navigateToLogin() {
        if (isRouterExist()) {
            router.showLogin();
        }
    }

    private void updateData(List<TicketItem> items) {
        ticketItems = items;
        showData();
    }

    private void showData() {
        if (ticketItems == null) {
            ticketItems = getEmptyList();
        }

        if (isViewBinded()) {
            boolean empty = isDataEmpty();

            getView().hideLoading();
            getView().showData(ticketItems);
            getView().setContentVisible(!empty);
            getView().setNoContentVisible(empty);
        }
    }

    private ArrayList<TicketItem> getEmptyList() {
        return new ArrayList<>();
    }

    private boolean isDataEmpty() {
        return ticketItems == null || ticketItems.isEmpty();
    }


    @Override
    public void onRefresh() {
        loadTickets();
    }

    @Override
    public void onLogout() {
        navigateToLogin();
    }

    @Override
    public void setRouter(LoginRouter router) {
        this.router = router;
        if (router != null) {
            router.setListener(this);
        }
    }

    private boolean isRouterExist() {
        return this.router != null;
    }


    @Override
    public void onLoginShown() {

    }

    @Override
    public void onLoginHidden() {
        loadTickets();
    }
}
