class BackupThread extends Thread {
    public static OutputStream interruptibleOutputStream(RandomAccessFile f_out) {
        return Channels.newOutputStream(f_out.getChannel());
    }
}
