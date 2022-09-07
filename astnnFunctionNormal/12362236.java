class BackupThread extends Thread {
    private DefaultMutableTreeNode createNodes() {
        DefaultMutableTreeNode top;
        if (!connected) top = new DefaultMutableTreeNode("UNCONNECTED (attempting to connect to " + getServer() + ")"); else {
            top = new DefaultMutableTreeNode("Connected to " + getServer());
            ChannelTree ct = getChannelTree();
            if (ct == null) {
                top = new DefaultMutableTreeNode("No Channel Tree (connection dropped?)");
            } else {
                Iterator i = ct.rootIterator();
                while (i.hasNext()) {
                    top.add(makeNodes((ChannelTree.Node) i.next()));
                }
            }
        }
        return top;
    }
}
