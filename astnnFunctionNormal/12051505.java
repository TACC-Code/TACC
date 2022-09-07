class BackupThread extends Thread {
    private void writeEntry(TarOutputStream tarOutputStream, String fileID, File file) throws IOException {
        TarEntry entry = new TarEntry(slashify(fileID));
        entry.setSize(file.length());
        tarOutputStream.putNextEntry(entry);
        FileInputStream fis = new FileInputStream(file);
        try {
            byte[] buf = newBuffer(bufferSize);
            int read;
            while ((read = fis.read(buf)) > 0) {
                tarOutputStream.write(buf, 0, read);
            }
        } finally {
            try {
                fis.close();
            } catch (IOException ignored) {
                logger.warn(MessageFormat.format(COULD_NOT_CLOSE_FILE_INPUT_STREAM, file), ignored);
            }
        }
        tarOutputStream.closeEntry();
    }
}
