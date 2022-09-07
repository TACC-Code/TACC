class BackupThread extends Thread {
    public int write(byte[] data, int ofs, int len) {
        int maxTries = 10;
        int totWrite = 0;
        int nofs = ofs, nlen = len;
        while (nofs < ofs + len && maxTries > 0) {
            int written = 0;
            if (format.getBits() == 16 && format.getChannels() == 2 && format.getRate() == INPUT_RATE) written = sdl.write(data, nofs, nlen); else written = writeConv(data, nofs, nlen);
            nofs += written;
            nlen -= written;
            totWrite += written;
            maxTries--;
            deliveredData += written;
            int deliveredTime = (int) (1000 * deliveredData / (INPUT_RATE * 4));
            long pos = sdl.getMicrosecondPosition() / 1000;
            long sleepTime = deliveredTime - pos - bufferTime;
            if (sleepTime < 0) sleepTime = 0;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
        }
        return totWrite;
    }
}
