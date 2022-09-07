class BackupThread extends Thread {
    public float getMaxSampleValue() {
        float m = 0;
        for (int i = 0; i < getNumberOfElements(); i++) {
            float s = getChannel(i).getMaxSampleValue();
            if (s > m) m = s;
        }
        return m;
    }
}
