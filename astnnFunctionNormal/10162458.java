class BackupThread extends Thread {
    public Nodes toNodes() {
        Nodes nodes = new Nodes();
        InputTable nodeTable = getTableNamed("NODE_GIS");
        if (nodeTable == null) {
            nodeTable = new InputTable();
            nodeTable.setName("NODE_GIS");
            nodeTable.setHeaders(Arrays.asList(new String[] { "ID", "LAT_LNG" }));
            Channels channels = toChannels();
            HashMap<String, String> nodeIdMap = new HashMap<String, String>();
            ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
            for (Channel channel : channels.getChannels()) {
                String nodeId = channel.getUpNodeId();
                if (!nodeIdMap.containsKey(nodeId)) {
                    nodeIdMap.put(nodeId, nodeId);
                    ArrayList<String> rowValue = new ArrayList<String>();
                    rowValue.add(nodeId);
                    int idValue = Integer.parseInt(nodeId);
                    double lat = 37.67 + 0.0001 * idValue;
                    double lng = -121.45 + 0.0001 * idValue;
                    rowValue.add("(" + lat + "," + lng + ")");
                    values.add(rowValue);
                }
            }
            nodeTable.setValues(values);
        }
        int nnodes = nodeTable.getValues().size();
        for (int i = 0; i < nnodes; i++) {
            Node node = new Node();
            node.setId(nodeTable.getValue(i, "ID"));
            String latLng = nodeTable.getValue(i, "LAT_LNG");
            node.setLatitude(TableUtil.toLatitude(latLng));
            node.setLongitude(TableUtil.toLongitude(latLng));
            nodes.addNode(node);
        }
        InputTable channelTable = getTableNamed("CHANNEL");
        if (channelTable == null) {
            return nodes;
        }
        int nchannels = channelTable.getValues().size();
        for (int i = 0; i < nchannels; i++) {
            checkAndAddNode(nodes, channelTable.getValue(i, "UPNODE"));
            checkAndAddNode(nodes, channelTable.getValue(i, "DOWNNODE"));
        }
        return nodes;
    }
}
