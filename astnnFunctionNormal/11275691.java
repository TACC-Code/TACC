class BackupThread extends Thread {
    public static InputStream openStream(String path) throws FileNotFoundException {
        if (path.toLowerCase().startsWith("http://")) {
            try {
                URL url = new URL(path);
                URLConnection urlConn = url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setUseCaches(true);
                byte[] b = null;
                long size = 0;
                InputStream is = urlConn.getInputStream();
                ByteArrayOutputStream os;
                if (path.toLowerCase().endsWith(".zip")) {
                    ZipInputStream zis = new ZipInputStream(is);
                    ZipEntry entry = zis.getNextEntry();
                    is = zis;
                }
                return is;
            } catch (Throwable e) {
            }
            throw new FileNotFoundException(path);
        } else if (path.toLowerCase().startsWith("jar://")) {
            path = path.substring(6);
            InputStream is = Dosbox.class.getResourceAsStream(path);
            if (is == null) {
                throw new FileNotFoundException();
            }
            return is;
        } else {
            path = FileHelper.resolve_path(path);
            return new FileInputStream(path);
        }
    }
}
