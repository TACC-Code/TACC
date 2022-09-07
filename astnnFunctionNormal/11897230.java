class BackupThread extends Thread {
    private URL testUrl(URL url) {
        if (url != null) {
            InputStream inputStream = null;
            try {
                inputStream = url.openStream();
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
            return url;
        }
        return null;
    }
}
