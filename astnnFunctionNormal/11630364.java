class BackupThread extends Thread {
        protected boolean copyStreamToFile(final InputStream source, final File target) throws IOException {
            final byte[] buffer = new byte[1024 * 4];
            final OutputStream out = new FileOutputStream(target);
            int read = 0;
            while ((read = source.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                }
            }
            return true;
        }
}
