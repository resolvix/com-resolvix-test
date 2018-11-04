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

    private String path;

    private List<Node> nodes;

    private void pathToNodeList(
        String path, int nodeSeparator,
        int fromIndex, List<Node> toNodes,
        int nodeIndex)
    {

        int toIndex = path.indexOf(nodeSeparator, fromIndex);
        String nodePath = (nodeIndex == 0 || toIndex != -1)
            ? path
            : path.substring(0, fromIndex);
        if (toIndex == -1) {
            toNodes.add(new NodeImpl(
                nodePath,
                path.substring(fromIndex),
                nodeIndex));
        } else {
            toNodes.add(new NodeImpl(
                nodePath,
                path.substring(fromIndex, toIndex),
                nodeIndex));
            pathToNodeList(path, nodeSeparator, toIndex + 1, toNodes, nodeIndex +1);
        }
    }

    private PathImpl(String path, int separator) {
        this.path = path;
        this.nodes = new LinkedList<>();
        pathToNodeList(path, separator, 0, nodes, 0);
    }

    private PathImpl(String path) {
        this(path, DEFAULT_NODE_SEPARATOR);
    }

    public static PathImpl of(String path, int separator) {
        return new PathImpl(path, separator);
    }

    public static PathImpl of(String path) {
        return new PathImpl(path);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}
