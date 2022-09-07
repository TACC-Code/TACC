class BackupThread extends Thread {
    void sendRawLine(String line) {
        OutputThread.sendRawLine(_bot, _bwriter, line);
    }
}
