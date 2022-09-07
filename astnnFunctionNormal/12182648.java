class BackupThread extends Thread {
    public void rename(final File file) {
        try {
            final File ff = getFile();
            if (!ff.renameTo(file)) {
                FileUtils.copyFile(ff, file, true);
                deleteFile(ff);
            }
            update(file);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
