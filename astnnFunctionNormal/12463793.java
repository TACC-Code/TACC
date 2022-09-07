class BackupThread extends Thread {
    private TreeNode[] getSiteChannelTree(String startPath) throws Exception {
        List list = new ArrayList();
        if (startPath.length() == 5) {
            TreeNode[] sites = getList(startPath);
            if (sites == null || sites.length == 0) {
                return null;
            }
            for (int i = 0; i < sites.length; i++) {
                if (sites[i] == null) {
                    continue;
                }
                TreeNode[] channels = getChannelTree(sites[i].getPath());
                list.add(sites[i]);
                for (int j = 0; channels != null && j < channels.length; j++) {
                    if (channels[j] != null) {
                        list.add(channels[j]);
                    }
                }
            }
        }
        if (startPath.length() == 10) {
            TreeNode site = getTreeNode(startPath);
            if (site == null) {
                throw new Exception("û���ҵ�path=" + startPath + "�Ķ���!");
            }
            TreeNode[] channels = getChannelTree(site.getPath());
            list.add(site);
            for (int j = 0; channels != null && j < channels.length; j++) {
                if (channels[j] != null) {
                    list.add(channels[j]);
                }
            }
        }
        if (startPath.length() > 10) {
            TreeNode[] channels = getChannelTree(startPath);
            for (int j = 0; channels != null && j < channels.length; j++) {
                if (channels[j] != null) {
                    list.add(channels[j]);
                }
            }
        }
        if (list.size() == 0) {
            return null;
        }
        TreeNode[] result = new TreeNode[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (TreeNode) list.get(i);
        }
        return result;
    }
}
