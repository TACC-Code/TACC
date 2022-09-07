class BackupThread extends Thread {
    public TreeNode[] getList(String path) throws Exception {
        final int SITE_LIST = 0;
        final int CHANNEL_LIST = 1;
        final int DOC_TYPE_LIST = 2;
        final int NON_LIST = -1;
        if (path == null) {
            return null;
        }
        List list = new ArrayList();
        int treeType = NON_LIST;
        if (path.equals(TreeNode.SITECHANNEL_TREE_ROOT_PATH)) {
            treeType = SITE_LIST;
        } else if (path.length() >= 5 && path.substring(0, 5).equals(TreeNode.SITECHANNEL_TREE_ROOT_PATH)) {
            treeType = CHANNEL_LIST;
        } else if (path.equals(TreeNode.DOCTYPE_TREE_ROOT_PATH) || (path.length() >= 5 && path.substring(0, 5).equals(TreeNode.DOCTYPE_TREE_ROOT_PATH))) {
            treeType = DOC_TYPE_LIST;
        } else {
            throw new Exception("�޷�ʶ���path(" + path + ")!");
        }
        if (treeType == SITE_LIST) {
            return getSiteList();
        } else if (treeType == CHANNEL_LIST) {
            return getChannelList(path);
        } else if (treeType == DOC_TYPE_LIST) {
            return getDocTypeList(path);
        } else {
            return null;
        }
    }
}
