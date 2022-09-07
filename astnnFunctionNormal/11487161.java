class BackupThread extends Thread {
    synchronized void requestByte(final boolean forRead) throws java.io.IOException {
        if ((!forRead || !(writeIn.available() > 0)) && (forRead || eager)) {
            int c = source.read();
            synchronized (writeIn) {
                readOut.write(c);
            }
        }
    }
}
