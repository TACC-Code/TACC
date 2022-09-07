class BackupThread extends Thread {
    public int get(ScrEvent e) {
        return e.getChannel();
    }
}
