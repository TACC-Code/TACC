class BackupThread extends Thread {
    public void testTreeReader() {
        URL url = TreeMap.class.getResource(TREE_CHI);
        Tree t = null;
        try {
            GZIPInputStream gzin = new GZIPInputStream(url.openStream());
            t = (Tree) new TreeMLReader().readGraph(gzin);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(true, t.isValidTree());
        Node[] nodelist = new Node[t.getNodeCount()];
        Iterator nodes = t.nodes();
        for (int i = 0; nodes.hasNext(); ++i) {
            nodelist[i] = (Node) nodes.next();
        }
        assertEquals(false, nodes.hasNext());
    }
}
