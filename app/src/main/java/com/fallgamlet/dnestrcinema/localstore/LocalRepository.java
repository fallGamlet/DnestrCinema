package com.fallgamlet.dnestrcinema.localstore;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fallgamlet on 23.07.17.
 */

public interface LocalRepository<T> {

    void save(T item);

    Observable<List<T>> getItems();

}
