class BackupThread extends Thread {
    public static InputStream interruptibleInputStream(FileInputStream f_in) {
        return Channels.newInputStream(f_in.getChannel());
    }
}
