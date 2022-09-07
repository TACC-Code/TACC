class BackupThread extends Thread {
    public static Reader interruptibleReader(FileInputStream f_in, String csName) {
        return Channels.newReader(f_in.getChannel(), csName);
    }
}
