class BackupThread extends Thread {
    public static OutputStream interruptibleOutputStream(FileOutputStream f_out) {
        return Channels.newOutputStream(f_out.getChannel());
    }
}
