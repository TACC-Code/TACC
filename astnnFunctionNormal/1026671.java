class BackupThread extends Thread {
    public static int readFileIndex() {
        final FileWriter writer = new FileWriter();
        return writer.readFileIndex();
    }
}
