class BackupThread extends Thread {
    public static TerminatedInputStream openStream(URL url, String entryName) throws IOException {
        if (url.getProtocol().equals("file")) {
            return openStream(new File(url.getFile()), entryName);
        } else {
            return openStream(url.openStream(), entryName);
        }
    }
}
