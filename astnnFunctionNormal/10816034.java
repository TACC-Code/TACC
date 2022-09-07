class BackupThread extends Thread {
    protected void commit() {
        new Breakpoint(what.getText(), read.isSelected(), write.isSelected());
        dispose();
    }
}
