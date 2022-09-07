class BackupThread extends Thread {
    private void initData() {
        NetWorkManager.getChannelList(this);
    }
}
