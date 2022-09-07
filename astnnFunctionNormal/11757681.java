class BackupThread extends Thread {
    private static void installTo(final URL urlResource, final File toFile) {
        final InputStream is;
        try {
            is = urlResource.openStream();
        } catch (IOException e) {
            final RuntimeException re = new RuntimeException("I/O Exception reading url: " + urlResource + ", " + e, e);
            log(re.getMessage(), e);
            throw re;
        }
        try {
            toFile.createNewFile();
        } catch (IOException e) {
            final RuntimeException re = new RuntimeException("I/O Exception creating file: " + toFile.getAbsolutePath() + ", " + e, e);
            log(re.getMessage(), e);
            throw re;
        }
        final OutputStream os;
        byte[] buf = new byte[255];
        try {
            os = new FileOutputStream(toFile);
            try {
                int len;
                while ((len = is.read(buf)) > 0) {
                    os.write(buf, 0, len);
                }
            } finally {
                os.close();
            }
        } catch (IOException e) {
            final RuntimeException re = new RuntimeException("I/O Exception writing file: " + toFile.getAbsolutePath() + ", " + e, e);
            log(re.getMessage(), e);
            throw re;
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
    }
}
