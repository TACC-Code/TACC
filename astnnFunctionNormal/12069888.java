class BackupThread extends Thread {
    public static Reader interruptibleReader(RandomAccessFile f_in) {
        return Channels.newReader(f_in.getChannel(), Charset.defaultCharset().name());
    }
}
