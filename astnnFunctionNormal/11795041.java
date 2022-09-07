class BackupThread extends Thread {
    public void sayGlobal(String string) {
        for (Iterator iter = new ArrayList(Collections.list(getIRCConnection().getClientState().getChannelNames())).iterator(); iter.hasNext(); ) {
            say((String) iter.next(), string);
        }
    }
}
