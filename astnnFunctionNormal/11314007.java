class BackupThread extends Thread {
    public static byte[] getFileBytes(URL _url) {
        try {
            InputStream input = _url.openStream();
            byte[] content = IOUtils.toByteArray(input);
            IOUtils.closeQuietly(input);
            return content;
        } catch (Exception err) {
            throw new RuntimeException(err);
        }
    }
}
