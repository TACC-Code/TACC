class BackupThread extends Thread {
    public WikiupProperties() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("wikiup/wikiup.properties");
        properties = new Properties();
        if (url != null) {
            try {
                InputStream is = url.openStream();
                properties.load(is);
                is.close();
            } catch (IOException ex) {
            }
        }
        init(properties);
    }
}
