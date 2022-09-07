class BackupThread extends Thread {
    public int read(byte[] buffer) {
        int len;
        write2debug("> URLDataFetcher.read(" + buffer + "): ");
        if (!initVars()) {
            return -1;
        }
        try {
            len = is.read(buffer);
        } catch (Exception X) {
            try {
                s.close();
            } catch (IOException ioXX) {
            }
            s = null;
            is = null;
            write2debug("-1\n");
            return -1;
        }
        if (len == 0) {
            len = -1;
        }
        write2debug(len + "\n");
        return len;
    }
}
