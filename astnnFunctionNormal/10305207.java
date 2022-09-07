class BackupThread extends Thread {
    public static Document build(URL url) {
        try {
            return build(url.openStream());
        } catch (IOException e) {
            logger.error("Fail to build dom from " + url, e);
            return null;
        }
    }
}
