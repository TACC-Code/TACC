class BackupThread extends Thread {
    private void addMenu() {
        JMenuBar bar = new JMenuBar();
        bar.add(getFileMenu());
        bar.add(getChannelMenu());
        bar.add(getDeviceControlMenu());
        setJMenuBar(bar);
    }
}
