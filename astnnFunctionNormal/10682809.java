class BackupThread extends Thread {
    private void unpackFile(final File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                File entryFile = new File(destDir, entry.getName());
                if (entryFile.exists()) continue;
                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                    if (!entryFile.exists() || !entryFile.isDirectory()) throw new IOException("Couldn't create directory");
                } else {
                    InputStream in = zipFile.getInputStream(entry);
                    OutputStream out = new FileOutputStream(entryFile);
                    byte[] buffer = new byte[32768];
                    int size;
                    while ((size = in.read(buffer)) > 0) out.write(buffer, 0, size);
                    in.close();
                    out.close();
                }
            }
            zipFile.close();
        } catch (final IOException ex) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        JOptionPane.showMessageDialog(null, "Temporary Zip-File " + file + " couldn't be extracted: " + ex, "Extraction Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex2) {
            }
            System.exit(1);
        }
    }
}
