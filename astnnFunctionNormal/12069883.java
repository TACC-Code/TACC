class BackupThread extends Thread {
    public static InputStream interruptibleSystemIn() {
        return Channels.newInputStream((new FileInputStream(FileDescriptor.in)).getChannel());
    }
}
