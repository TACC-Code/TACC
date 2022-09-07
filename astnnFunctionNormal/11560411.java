class BackupThread extends Thread {
    public static boolean urlStrIsDir(String urlStr) {
        if (urlStr.endsWith("/")) return true;
        String urlStrWithSlash = urlStr + "/";
        try {
            URL url = new URL(urlStrWithSlash);
            InputStream f = url.openStream();
            f.close();
            return true;
        } catch (Exception _ex) {
            return false;
        }
    }
}
