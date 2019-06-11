package com.fallgamlet.dnestrcinema.data.localstore;

/**
 * Created by fallgamlet on 23.07.17.
 */

public interface Filter <T> {

    boolean check(T item);

}
