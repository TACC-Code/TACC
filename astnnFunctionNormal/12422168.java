class BackupThread extends Thread {
    public static boolean checkURL(URL url) {
        boolean success = false;
        InputStream is = null;
        try {
            is = url.openStream();
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
