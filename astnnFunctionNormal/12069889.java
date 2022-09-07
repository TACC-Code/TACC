class BackupThread extends Thread {
    public static Reader interruptibleReader(RandomAccessFile f_in, String csName) {
        return Channels.newReader(f_in.getChannel(), csName);
    }
}
