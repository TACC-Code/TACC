class BackupThread extends Thread {
    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        for (int i = 0; i < l1; i++) {
            float xu2 = (i + o1 - mean) * (i + o1 - mean);
            float d2 = 2 * deviation * deviation;
            float v = (float) (amplitude * 1 / Math.sqrt(2 * Math.PI * deviation) * Math.exp(-xu2 / d2));
            float s;
            if (add) {
                s = s1.get(o1 + i) + v;
            } else {
                s = v;
            }
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s));
        }
    }
}
