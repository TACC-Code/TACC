class BackupThread extends Thread {
    public int getRowCount() {
        if (_search != null) return _search.getChannelCount(); else return 0;
    }
}
