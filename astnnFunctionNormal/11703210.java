class BackupThread extends Thread {
    public static InputStream fetch(String base, String file) throws Exception {
        URL url = new URL(base + file);
        System.err.println("fetching " + url);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        return is;
    }
}
