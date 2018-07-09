package com.fallgamlet.dnestrcinema.network;

/**
 * Created by fallgamlet on 08.07.17.
 */

public interface Mapper <From, To> {

    To map(From src);

}
