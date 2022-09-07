class BackupThread extends Thread {
    protected void cleanUp() {
        if (getMidiOutDevice() != null) {
            getMidiOutDevice().close();
        }
        if (getSynthesizer() != null) {
            getSynthesizer().getChannels()[0].allNotesOff();
        }
    }
}
