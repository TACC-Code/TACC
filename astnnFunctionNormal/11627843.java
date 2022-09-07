class BackupThread extends Thread {
    public static InputStream pathToStream(String path) throws IOException, MalformedURLException {
        validatePath(path);
        URL url = pathToURL(path);
        if (url != null) {
            return url.openStream();
        }
        InputStream in = PathHelper.class.getResourceAsStream(path);
        if (in == null && !path.startsWith("/")) {
            in = PathHelper.class.getResourceAsStream("/" + path);
        }
        return in;
    }
}
