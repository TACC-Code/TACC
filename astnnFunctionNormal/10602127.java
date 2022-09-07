class BackupThread extends Thread {
    private void init() {
        channel = this.fos.getChannel();
        out = IoBuffer.allocate(1024);
        out.setAutoExpand(true);
    }
}
