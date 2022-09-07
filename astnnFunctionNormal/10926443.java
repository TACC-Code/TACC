class BackupThread extends Thread {
    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1, 0);
        ch1.getChannel().markChange();
        LProgressViewer.getInstance().entrySubProgress(0.7);
        tmp.copy(s1, o1, 0, l1);
        for (int i = 0; i < freq.length; i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / freq.length)) return;
            if (freq[i] != 0) {
                AOToolkit.setIirNotch(tmp, 0, l1, freq[i], q, 1);
            }
        }
        for (int i = 0; i < l1; i++) {
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), tmp.get(i)));
        }
        LProgressViewer.getInstance().exitSubProgress();
    }
}
