class BackupThread extends Thread {
    public static BufferedImage readImage(URL url) {
        InputStream in = null;
        try {
            URLConnection urlConnection = url.openConnection();
            in = urlConnection.getInputStream();
            return readImage(readStream(in));
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
