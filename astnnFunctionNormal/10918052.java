class BackupThread extends Thread {
    public ByteFileList(RandomAccessFile file) {
        super();
        this.file = file;
        this.channel = file.getChannel();
        size();
    }
}
