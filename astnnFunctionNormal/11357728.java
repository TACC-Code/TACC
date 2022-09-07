class BackupThread extends Thread {
    public void open(FileAccessMode accessMode) throws FileNotFoundException {
        if (opened) return;
        fileChannel = new RandomAccessFile(fileInfo.getFile(), accessMode.getMode()).getChannel();
        opened = true;
    }
}
