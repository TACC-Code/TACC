class BackupThread extends Thread {
    public ByteStreamFileChannel(FileInputStream fis) {
        this.fis = fis;
        channel = this.fis.getChannel();
    }
}
