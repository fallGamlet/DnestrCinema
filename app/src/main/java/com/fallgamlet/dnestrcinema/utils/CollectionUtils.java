package com.fallgamlet.dnestrcinema.utils;

import java.util.Collection;

/**
 * Created by fallgamlet on 23.07.17.
 */

public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static int getSize(Collection collection) {
        return collection == null? 0: collection.size();
    }
}
