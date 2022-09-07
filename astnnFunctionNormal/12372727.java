class BackupThread extends Thread {
    private static Properties loadButtonNameMap() {
        Properties p = new Properties();
        URL url = ResourceManager.getURLResource("icon_mapping.properties");
        InputStream is = null;
        try {
            if (url != null) {
                is = new BufferedInputStream(url.openStream());
                p.load(is);
            }
        } catch (IOException ignored) {
        } finally {
            IOUtils.close(is);
        }
        return p;
    }
}
