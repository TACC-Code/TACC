class BackupThread extends Thread {
    public static void unzip(URL url, File destDir) throws IOException {
        if (url == null) throw new IOException("URL cannot be null");
        InputStream is = url.openStream();
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(is));
            unzip(zis, destDir);
        } finally {
            try {
                if (zis != null) zis.close();
            } catch (Exception e) {
            }
        }
    }
}
