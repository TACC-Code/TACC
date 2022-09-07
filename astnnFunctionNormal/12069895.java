class BackupThread extends Thread {
    public static Writer interruptibleWriter(FileOutputStream f_out, String csName) {
        return Channels.newWriter(f_out.getChannel(), csName);
    }
}
