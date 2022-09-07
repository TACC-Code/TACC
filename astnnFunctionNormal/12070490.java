class BackupThread extends Thread {
    private final File copyLibraryToTemp(final URL url) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = url.openStream();
            final File file = File.createTempFile("lib", ".so");
            file.deleteOnExit();
            out = new BufferedOutputStream(new FileOutputStream(file));
            final byte buffer[] = new byte[2048];
            int inb = -1;
            while ((inb = in.read(buffer)) != -1) {
                out.write(buffer, 0, inb);
            }
            return file;
        } catch (final IOException e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (final IOException e) {
            }
            try {
                out.close();
            } catch (final IOException e) {
            }
        }
    }
}
