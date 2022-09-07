class BackupThread extends Thread {
    protected static long size(URL url, File file) {
        long size = 0;
        if (url != null) try {
            size = url.openConnection().getContentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file != null) size = file.length();
        return size;
    }
}
