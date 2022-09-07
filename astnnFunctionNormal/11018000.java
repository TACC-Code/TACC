class BackupThread extends Thread {
    public static void main(String[] argv) {
        URL url = TreeMap.class.getResource(TREE_CHI);
        Tree t = null;
        try {
            GZIPInputStream gzin = new GZIPInputStream(url.openStream());
            t = (Tree) new TreeMLReader().readGraph(gzin);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        JPrefuseTable table = new JPrefuseTable(t.getEdgeTable());
        JFrame frame = new JFrame("edges");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(table));
        frame.pack();
        frame.setVisible(true);
    }
}
