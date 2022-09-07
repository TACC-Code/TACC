class BackupThread extends Thread {
    private byte[] getImagedata(String oldImageUrl) {
        byte[] oldImageData = null;
        try {
            final URL url = new URL(oldImageUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            final InputStream inputStream = connection.getInputStream();
            int available = inputStream.available();
            oldImageData = new byte[available];
            int read = inputStream.read();
            int i = 0;
            while (read != -1 && i < available) {
                oldImageData[i] = (byte) read;
                read = inputStream.read();
                i++;
            }
            inputStream.close();
        } catch (Exception e) {
            LogUtils.logError(log, "ERROR: " + e.getMessage(), writer);
        }
        return oldImageData;
    }
}
