class BackupThread extends Thread {
    public void open() throws FileNotFoundException {
        raf = new RandomAccessFile(srcFile, "rw");
        chan = raf.getChannel();
        pos = 0;
        isOpened = true;
    }
}
