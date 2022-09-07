class BackupThread extends Thread {
    public void setInput(SliceReader writer) {
        reader.destroy();
        reader = writer;
    }
}
