class BackupThread extends Thread {
    public float getMaxSampleValue(int offset, int length) {
        float m = 0;
        for (int i = 0; i < getNumberOfElements(); i++) {
            float s = getChannel(i).getMaxSampleValue(offset, length);
            if (s > m) m = s;
        }
        return m;
    }
}
