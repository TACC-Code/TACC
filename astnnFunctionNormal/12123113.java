class BackupThread extends Thread {
    public String getCBFChannelName() {
        String result = "";
        try {
            DocumentCBF cbf = DocumentCBF.getInstance(this.id);
            if (cbf != null) {
                String channelPath = cbf.getChannelPath();
                Channel channel = (Channel) TreeNode.getInstance(channelPath);
                if (channel != null) {
                    result = channel.getName();
                }
            }
        } catch (Exception ex) {
            log.error("�õ��÷����ĵ��ĳ�ʼƵ�������ʧ�ܣ�");
        }
        return result;
    }
}
