class BackupThread extends Thread {
    private void writeThreadThrowable(final Thread t, final Throwable e) {
        writeString("Exception in thread " + t.getName() + "\r\n", _messageWarningAttributeSet);
        writeThrowable(e);
    }
}
