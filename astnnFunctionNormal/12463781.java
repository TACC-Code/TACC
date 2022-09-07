class BackupThread extends Thread {
    public TreeNode getTreeNode(String path) throws Exception {
        TreeNode result = null;
        int treeNodeType = this.getTreeNodeType(path);
        if (treeNodeType == TreeNode.TREENODE_TYPE_SITE) {
            result = this.getSiteInstance(path);
        } else if (treeNodeType == TreeNode.TREENODE_TYPE_CHANNEL) {
            result = this.getChannelInstance(path);
        } else if (treeNodeType == TreeNode.TREENODE_TYPE_DOCTYPE) {
            result = this.getDocTypeInstance(path);
        } else if (treeNodeType == TreeNode.TREENODE_TYPE_ROOT) {
            result = this.getRootInstance(path);
        }
        return result;
    }
}
