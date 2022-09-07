class BackupThread extends Thread {
    public void setPictureData(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            setPictureData(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
