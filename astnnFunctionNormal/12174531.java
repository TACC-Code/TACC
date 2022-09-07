class BackupThread extends Thread {
    public final void writeNext(String[] nextLine) {
        try {
            writer.writeNext(nextLine);
            writer.flush();
        } catch (IOException e) {
            WdLogs.error(e);
        }
    }
}
