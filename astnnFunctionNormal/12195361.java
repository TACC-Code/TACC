class BackupThread extends Thread {
    public List<URL> read() throws IOException {
        try {
            loadCount = 0;
            loadAlpha = 0;
            loadedURLs = new ArrayList<URL>();
            loadedURLs.add(url);
            sourceNode = graph.getNodeById(url.toString());
            if (sourceNode == null) {
                sourceNode = graph.getNodeOrAdd(url.toString());
                reload = false;
            } else {
                elementsToRemove = new ArrayList<GraphElement>(sourceNode.getNeighbours(NS.graphl + "definedIn", Node.REVERSE));
                elementsToRemove.remove(sourceNode);
                elementsToRemove.addAll(graph.getEdgesWithPropertyValue(NS.graphl + "definedIn", url.toString()));
                reload = true;
            }
            if (sourceNode.getNeighbours(NS.graphl + "definedIn", Node.FORWARD).size() == 0) {
                Edge edge = graph.createEdge(sourceNode, sourceNode);
                edge.setSource(NS.graphl + "SYSTEM");
                edge.setType(NS.graphl + "definedIn");
                graph.addElements(null, Collections.singleton(edge));
            }
            InputSource input;
            input = new InputSource(url.openConnection().getInputStream());
            input.setSystemId(url.toString());
            RDFParser parser = new RDFParser();
            loading = true;
            try {
                parser.parse(input, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            loading = false;
            if (reload) {
                for (GraphElement element : elementsToRemove) {
                    if (element instanceof Node) {
                        Node node = (Node) element;
                        List<Node> nodeSources = node.getNeighbours(NS.graphl + "definedIn", Node.FORWARD);
                        if (nodeSources.size() > 1) {
                            elementsToRemove.remove(node);
                        }
                    }
                }
                graph.removeElements(elementsToRemove);
            }
            return loadedURLs;
        } finally {
            loading = false;
            reload = false;
        }
    }
}
