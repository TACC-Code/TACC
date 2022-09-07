class BackupThread extends Thread {
    public static boolean checkURLPath(String path) {
        boolean success = false;
        InputStream is = null;
        try {
            URL urlPath = new URL(path);
            is = urlPath.openStream();
            success = true;
        } catch (MalformedURLException ex) {
            success = false;
        } catch (IOException ex) {
            success = false;
        } finally {
            close(is);
        }
        return success;
    }
}
