class BackupThread extends Thread {
    private static Font readFont(URL url) {
        Font font = null;
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
        return font;
    }
}
