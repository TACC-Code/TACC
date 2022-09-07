class BackupThread extends Thread {
    public FileTraceSource(File f) throws FileNotFoundException {
        this.file = f;
        fs = new FileInputStream(file);
        fc = fs.getChannel();
        try {
            fileSize = fc.size();
        } catch (IOException ioe) {
            Logging.errorln("Warning: Could not determine size of file " + f);
        }
    }
}
