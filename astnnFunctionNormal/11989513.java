class BackupThread extends Thread {
    public void init() throws InvalidContextException {
        if (internalSession == null) throw new InvalidContextException();
        this.speechClient = new SpeechClientImpl(internalSession.getTtsChannel(), internalSession.getRecogChannel());
        this.telephonyClient = new TelephonyClientImpl(externalSession.getChannelName());
    }
}
