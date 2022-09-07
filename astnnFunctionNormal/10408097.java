class BackupThread extends Thread {
    public SequentialTrainingFileReader(URL url) throws IOException {
        this(new DataInputStream(url.openStream()));
    }
}
