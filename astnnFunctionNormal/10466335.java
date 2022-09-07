class BackupThread extends Thread {
    public int getMaxSampleLength() {
        int m = 0;
        for (int i = 0; i < getNumberOfElements(); i++) {
            int s = getChannel(i).getSampleLength();
            if (s > m) m = s;
        }
        return m;
    }
}
