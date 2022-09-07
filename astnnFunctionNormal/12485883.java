class BackupThread extends Thread {
    public static boolean seemsToExist(URL url) {
        if (url.getProtocol().equals(FileManager.FILE)) {
            File file = new File(unescape(url.getFile()));
            return file.exists();
        }
        InputStream test = null;
        try {
            test = url.openStream();
        } catch (IOException e) {
            return false;
        }
        if (test != null) {
            try {
                test.close();
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
