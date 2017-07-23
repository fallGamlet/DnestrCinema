package com.fallgamlet.dnestrcinema.localstore;

/**
 * Created by fallgamlet on 23.07.17.
 */

public interface Filter <T> {

    boolean check(T item);

}
