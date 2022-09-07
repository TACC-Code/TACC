class BackupThread extends Thread {
    public void activateDaemons() {
        log.config("Activating Demons for: " + this);
        ChannelGuideSet theSet = GlobalModel.SINGLETON.getChannelGuideSet();
        theSet.getInformaBackEnd().activateMemoryChannels4CG(this);
        theSet.getInformaBackEnd().activatePersistentChannels4CG(this);
    }
}
