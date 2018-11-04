package com.resolvix.lib.reflect.impl;

import com.resolvix.lib.reflect.api.Path;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PathImpl implements Path {

    private static final int DEFAULT_NODE_SEPARATOR = '.';

    class NodeImpl implements Node {

        private String path;
        private String name;
        private int index;

        private NodeImpl(String path, String name, int index) {
            this.path = path;
            this.name = name;
            this.index = index;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Integer getIndex() {
            return index;
        }

        @Override
        public String getPath() {
            return path;
        }
    }

    private List<Node> nodes;

    private void pathToNodeList(
        String beanPath, int nodeSeparator,
        int fromIndex, List<Node> toNodes,
        int nodeIndex)
    {
        String nodePath = (nodeIndex == 0)
            ? beanPath
            : beanPath.substring(0, fromIndex);
        int toIndex = beanPath.indexOf(nodeSeparator, fromIndex);
        if (toIndex == -1) {
            toNodes.add(new NodeImpl(
                nodePath,
                beanPath.substring(fromIndex),
                nodeIndex));
        } else {
            toNodes.add(new NodeImpl(
                nodePath,
                beanPath.substring(fromIndex, toIndex),
                nodeIndex));
            pathToNodeList(beanPath, nodeSeparator, toIndex + 1, toNodes, nodeIndex +1);
        }
    }

    private PathImpl(String beanPath, int separator) {
        this.nodes = new LinkedList<>();
        pathToNodeList(beanPath, separator, 0, nodes, 0);
    }

    private PathImpl(String beanPath) {
        this(beanPath, DEFAULT_NODE_SEPARATOR);
    }

    public static PathImpl of(String beanPath, int separator) {
        return new PathImpl(beanPath, separator);
    }

    public static PathImpl of(String beanPath) {
        return new PathImpl(beanPath);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}
