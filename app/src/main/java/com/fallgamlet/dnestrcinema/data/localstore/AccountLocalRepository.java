package com.fallgamlet.dnestrcinema.data.localstore;

import android.content.Context;
import android.content.SharedPreferences;

import com.fallgamlet.dnestrcinema.data.localstore.pojo.AccountStore;
import com.fallgamlet.dnestrcinema.domain.models.AccountItem;
import com.fallgamlet.dnestrcinema.utils.StringUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class AccountLocalRepository implements LocalRepository <AccountItem> {

    private static final String FIELD = "accounts";
    private Context context;

    public AccountLocalRepository(Context context) {
        this.context = context;
    }

    @Override
    public void save(AccountItem accountItem) {
        AccountStore store = getStore(accountItem);
        save(store);
    }

    private AccountStore getStore(AccountItem accountItem) {
        AccountStore store = new AccountStore();
        store.setLogin(accountItem.getLogin());
        store.setPassword(accountItem.getPassword());
        store.setCinemaId(accountItem.getCinemaId());

        return store;
    }

    private void save(AccountStore accountStore) {
        String json = new Gson().toJson(accountStore);

        getSharedPreferences()
                .edit()
                .putString(FIELD, json)
                .apply();
    }


    @Override
    public Observable<List<AccountItem>> getItems() {
        final SharedPreferences prefs = getSharedPreferences();

        return Observable.just(prefs)
                .map(new Function<SharedPreferences, List<AccountItem>>() {
                    @Override
                    public List<AccountItem> apply(@NonNull SharedPreferences sharedPreferences) throws Exception {
                        String json = prefs.getString(FIELD, null);
                        return getAccounts(json);
                    }
                });
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(AccountStore.class.getName(), Context.MODE_PRIVATE);
    }

    private List<AccountItem> getAccounts(String json) {
        ArrayList<AccountItem> list = new ArrayList<>();

        if (StringUtils.INSTANCE.isEmpty(json)) {
            return list;
        }

        AccountStore accountStore = new Gson().fromJson(json, AccountStore.class);

        if (accountStore == null) {
            return list;
        }

        AccountItem accountItem = getAccount(accountStore);
        list.add(accountItem);

        return list;
    }

    private AccountItem getAccount(AccountStore item) {
        AccountItem accountItem = new AccountItem();
        accountItem.setLogin(item.getLogin());
        accountItem.setPassword(item.getPassword());
        accountItem.setCinemaId(item.getCinemaId());

        return accountItem;
    }

}
