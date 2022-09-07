class BackupThread extends Thread {
    public static final HUDFont getFont(URL url, FontStyle style, int size) throws IOException {
        if (url == null) throw new IllegalArgumentException("url must not be null");
        String name = url.toString();
        HUDFont font = MAP.get(getString(name, style, size));
        if (font == null) {
            InputStream is = url.openStream();
            is.close();
            font = new HUDFont(url, name, style, size);
            MAP.put(getString(name, style, size), font);
        }
        return (font);
    }
}
