package com.fallgamlet.dnestrcinema.mergers;

/**
 * Created by fallgamlet on 23.07.17.
 */

public interface Merger <T> {

    void merge(T to, T from);

}
