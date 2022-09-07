class BackupThread extends Thread {
    public static InputStream interruptibleInputStream(RandomAccessFile f_in) {
        return Channels.newInputStream(f_in.getChannel());
    }
}
