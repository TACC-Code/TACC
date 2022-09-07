class BackupThread extends Thread {
    public InputStream getElementIncludeSource(String id, String name, Attributes atts) throws Exception {
        URL url = getIncludeURL(atts.getValue("SRC"));
        return url.openStream();
    }
}
