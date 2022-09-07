class BackupThread extends Thread {
    protected Patch createNewPatch() {
        Patch p = new Patch(Constants.NEW_SINGLE_SYSEX, this);
        p.sysex[4] = (byte) getChannel();
        return p;
    }
}
