class BackupThread extends Thread {
    public AudioFormat getCircBufFormat() {
        AudioFormat res = getSourceFormat();
        if (AudioUtils.isPCM(res)) return res;
        return new AudioFormat(res.getSampleRate(), 16, res.getChannels(), true, false);
    }
}
