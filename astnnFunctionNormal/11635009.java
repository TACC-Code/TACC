class BackupThread extends Thread {
    public static Font getFont(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        Font font = fonts.get(url);
        if (font == null) {
            try {
                InputStream input = url.openStream();
                try {
                    font = Font.createFont(Font.TRUETYPE_FONT, input);
                } finally {
                    try {
                        input.close();
                    } catch (IOException ignore) {
                    }
                }
            } catch (Exception e) {
            }
            fonts.put(url, font);
        }
        return font;
    }
}
