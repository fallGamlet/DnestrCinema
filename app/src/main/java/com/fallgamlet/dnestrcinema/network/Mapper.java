package com.fallgamlet.dnestrcinema.network;

import java.util.Collection;
import java.util.List;

/**
 * Created by fallgamlet on 08.07.17.
 */

public interface Mapper <From, To> {

    To map(From src);

}