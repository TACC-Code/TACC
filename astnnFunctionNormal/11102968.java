class BackupThread extends Thread {
    public static ScatteringByteChannel getChannel(InputStream in) {
        if (in instanceof FileInputStream) {
            return ((FileInputStream) in).getChannel();
        } else {
            return new InputStreamChannel(in);
        }
    }
}
