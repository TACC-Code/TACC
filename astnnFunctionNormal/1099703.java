class BackupThread extends Thread {
    public InputStream getInputStream(String name) {
        for (URL url : urls) try {
            URL url2 = new URL(url, name);
            InputStream in = url2.openStream();
            if (in != null) return in;
        } catch (Throwable xp) {
        }
        return null;
    }
}
