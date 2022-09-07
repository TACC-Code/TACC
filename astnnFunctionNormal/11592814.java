class BackupThread extends Thread {
    public Object getChannelObject() throws XAwareException {
        throw new XAwareException(className + ".getChannelObject() is not implemented instead use getChannelTemplate()");
    }
}
