class BackupThread extends Thread {
    public LogReader(DBBroker broker, File file, int fileNumber) throws LogException {
        this.broker = broker;
        this.fileNumber = fileNumber;
        try {
            FileInputStream is = new FileInputStream(file);
            fc = is.getChannel();
            mapped = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        } catch (IOException e) {
            throw new LogException("Failed to read log file " + file.getAbsolutePath(), e);
        }
    }
}
