package com.resolvix.lib.reflect.api;

import java.util.Iterator;

public interface Path {

    interface Node {
        String getName();
        Integer getIndex();
        String getPath();
    }

    String getPath();

    Iterator<Node> iterator();
}
