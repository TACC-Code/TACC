class BackupThread extends Thread {
    public void save(List<ErazeMethod> methods, File file) throws FileNotFoundException, IOException {
        FileOutputStream os = null;
        FileChannel channel = null;
        try {
            os = new FileOutputStream(file);
            channel = os.getChannel();
            writeHeader(channel, methods);
            writeMethods(channel, methods);
        } finally {
            closeOutputStream(os);
        }
    }
}
