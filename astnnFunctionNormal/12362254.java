class BackupThread extends Thread {
    private boolean sameTree(JTree tree) {
        TreeModel m = tree.getModel();
        if (m == null) return false;
        ChannelTree ct = getChannelTree();
        if (ct == null) return false;
        return sameTreeNodes(ct.rootIterator(), (DefaultMutableTreeNode) m.getRoot());
    }
}
