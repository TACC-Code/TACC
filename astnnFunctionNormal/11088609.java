class BackupThread extends Thread {
    public static byte[] getBytes(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            return getBytes(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
