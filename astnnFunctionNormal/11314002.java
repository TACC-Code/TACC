class BackupThread extends Thread {
    public static String getFileText(URL _url, String _encoding) {
        try {
            InputStream input = _url.openStream();
            String content = IOUtils.toString(input, _encoding);
            IOUtils.closeQuietly(input);
            return content;
        } catch (Throwable err) {
            LOG.error(_url.toString(), err);
            return "";
        }
    }
}
