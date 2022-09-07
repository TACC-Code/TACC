class BackupThread extends Thread {
    public byte[] getAsByteArray(final String name) throws IOException {
        byte[] buffer;
        URL url = ResourceUtils.getAsUrl(name);
        if (url != null) {
            InputStream is = url.openStream();
            buffer = readInputStream(is);
            is.close();
            return buffer;
        }
        FileInputStream fis = new FileInputStream(name);
        buffer = readInputStream(fis);
        fis.close();
        return buffer;
    }
}
