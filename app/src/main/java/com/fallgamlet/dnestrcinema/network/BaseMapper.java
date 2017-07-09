package com.fallgamlet.dnestrcinema.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fallgamlet on 08.07.17.
 */

public abstract class BaseMapper<From, To> implements Mapper<From, To> {

    public List<To> mapList(Collection<From> srcCollection) {
        ArrayList<To> destList = new ArrayList<>();

        for (From item: srcCollection) {
            To res = map(item);
            destList.add(res);
        }

        return destList;
    }

}
